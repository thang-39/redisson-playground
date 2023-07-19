package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec13BatchTest extends BaseTest {

    @Test // 20000: 1s387 | 500000: 7s311
    public void batchTest() {
        RBatchReactive batch = this.client.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);

        for (long i = 0; i < 500_000; i++) {
            list.add(i);
            set.add(i);
        }

        StepVerifier.create(batch.execute().then())
                .verifyComplete();

    }

    @Test // 500000: 31s35
    public void regularTest() {

        RListReactive<Long> list = client.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = client.getSet("numbers-set", LongCodec.INSTANCE);

        Mono<Void> mono = Flux.range(1, 500000)
                .map(i -> Long.valueOf(i))
                .flatMap(i -> list.add(i).then(set.add(i)))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
    }
}
