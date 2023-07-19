package org.example.redisson.test;

import org.example.redisson.test.config.RedissonConfig;
import org.example.redisson.test.dto.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Lec08LocalCachedMapTest extends BaseTest {

    private RLocalCachedMap<Integer, Student> studentMap;

    @BeforeAll
    public void setupClient() {
        RedissonConfig config = new RedissonConfig();
        RedissonClient redissonClient = config.getRedissonClient();

        LocalCachedMapOptions<Integer, Student> mapOptions = LocalCachedMapOptions.<Integer,Student>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE);

        this.studentMap = redissonClient.getLocalCachedMap(
                "students",
                new TypedJsonJacksonCodec(Integer.class, Student.class),
                mapOptions
        );
    }

    @Test
    public void appServer1() {
        Student student1 = new Student("sam",10,"atlanta", Arrays.asList(1,2,3));
        Student student2 = new Student("jake",30,"miami", List.of(10,20,30));

        this.studentMap.put(1, student1);
        this.studentMap.put(2, student2);

        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println(i + " ==> " + studentMap.get(1)))
                .subscribe();

        sleep(600000);
    }

    @Test
    public void appServer2() {
        Student student1 = new Student("sam-updated",10,"atlanta", Arrays.asList(1,2,3));

        this.studentMap.put(1, student1);

    }
}
