package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec04BucketAsMapTest extends BaseTest{

    // user:1:name
    // user:2:name

    @Test
    public void bucketsAsMap() {
        Mono<Void> mono = this.client.getBuckets(StringCodec.INSTANCE)
                .get("user:1:name", "user:2:name", "user:3:name", "user:4:name")
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
