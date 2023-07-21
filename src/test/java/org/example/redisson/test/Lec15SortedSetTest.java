package org.example.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec15SortedSetTest extends BaseTest {

    @Test
    public void sortedSet() {
        RScoredSortedSetReactive<String> sortedSet = client.getScoredSortedSet("student:score", StringCodec.INSTANCE);

        Mono<Void> mono = sortedSet.addScore("sam", 12.25) // add more to existing item or create new with this value
                .then(sortedSet.add(23.25, "mike")) // replace the existing item or create new
                .then(sortedSet.addScore("jake", 7))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();

        sortedSet.entryRange(0,1)
                .flatMapIterable(Function.identity())
                .map(se -> se.getScore() + " : " + se.getValue())
                .doOnNext(System.out::println)
                .subscribe();
        sleep(1000);
    }
}
