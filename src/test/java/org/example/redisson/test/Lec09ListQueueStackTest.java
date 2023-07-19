package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.LongStream;

public class Lec09ListQueueStackTest extends BaseTest {

    @Test
    public void listTest() {
        // lrange number-input 0 -1
        RListReactive<Long> list = this.client.getList("number-input", LongCodec.INSTANCE);
        Mono<Void> listAdd = Flux.range(1, 10)
                .map(i -> Long.valueOf(i))
                .flatMap(i -> list.add(i))
                .then();
        StepVerifier.create(listAdd)
                .verifyComplete();
        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }

    @Test
    public void listTest2() {
        // lrange number-input 0 -1
        RListReactive<Long> list = this.client.getList("number-input2", LongCodec.INSTANCE);

        List<Long> longList = LongStream.rangeClosed(1,10)
                        .boxed().toList();

        StepVerifier.create(list.addAll(longList).then())
                .verifyComplete();

        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }

    @Test
    public void queueTest() {
        // lrange number-input 0 -1
        RQueueReactive<Long> queue = this.client.getQueue("number-input2", LongCodec.INSTANCE);

        Mono<Void> queuePoll = queue.poll() // 1
                .repeat(3)
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(queuePoll)
                .verifyComplete();
        StepVerifier.create(queue.size())
                .expectNext(6)
                .verifyComplete();
    }

    @Test
    public void stackTest() { // Deque
        // lrange number-input 0 -1
        RDequeReactive<Long> deque = this.client.getDeque("number-input2", LongCodec.INSTANCE);

        Mono<Void> stackPoll = deque.pollLast() // 1
                .repeat(3)
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(stackPoll)
                .verifyComplete();
        StepVerifier.create(deque.size())
                .expectNext(2)
                .verifyComplete();
    }
}
