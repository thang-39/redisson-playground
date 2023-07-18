package org.example.redisson.test;

import org.example.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec07MapCacheTest extends BaseTest{

    @Test
    public void mapCacheTest() {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> mapCache = this.client.getMapCache("users:cache", codec);

        Student student1 = new Student("sam",10,"atlanta", Arrays.asList(1,2,3));
        Student student2 = new Student("jake",30,"miami", List.of(10,20,30));

        Mono<Student> mono1 = mapCache.put(1, student1,5, TimeUnit.SECONDS);
        Mono<Student> mono2 = mapCache.put(2, student2,10, TimeUnit.SECONDS);

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();

        sleep(3000);

        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

        sleep(3000);

        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();
    }

}
