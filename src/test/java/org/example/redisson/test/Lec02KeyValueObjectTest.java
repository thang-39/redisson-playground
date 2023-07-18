package org.example.redisson.test;

import org.example.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

public class Lec02KeyValueObjectTest extends BaseTest{

    @Test
    public void keyValueObjectBinaryTest() {
        Student student = new Student("marshal",10,"atlanta", Arrays.asList(1,2,3));
        RBucketReactive<Object> bucket = this.client.getBucket("student:1");
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

    }

    @Test
    public void keyValueObjectJsonTest() {
        Student student = new Student("marshal",10,"atlanta",Arrays.asList(1,2,3));
        RBucketReactive<Object> bucket = this.client.getBucket("student:1", JsonJacksonCodec.INSTANCE);
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueObjectJson2Test() {
        Student student = new Student("marshal",10,"atlanta",Arrays.asList(1,2,3));
        RBucketReactive<Object> bucket = this.client.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        Mono<Void> set = bucket.set(student);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
