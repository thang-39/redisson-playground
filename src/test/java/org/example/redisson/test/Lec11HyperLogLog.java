package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.LongStream;

public class Lec11HyperLogLog extends BaseTest {

    @Test
    public void count() {
        // HyperLogLog 12.5kb
        RHyperLogLogReactive<Long> counter = this.client
                .getHyperLogLog("user:visit", LongCodec.INSTANCE);
        List<Long> longList1 = LongStream.rangeClosed(1, 25000)
                .boxed()
                .toList();

        List<Long> longList2 = LongStream.rangeClosed(25001, 50000)
                .boxed()
                .toList();

        List<Long> longList3 = LongStream.rangeClosed(1, 75000)
                .boxed()
                .toList();

        List<Long> longList4 = LongStream.rangeClosed(50000, 100000)
                .boxed()
                .toList();

        Mono<Void> mono = Flux.just(longList1, longList2, longList3, longList4)
                .flatMap(list -> counter.addAll(list))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        counter.count()
                .doOnNext(System.out::println)
                .subscribe();

    }
}
