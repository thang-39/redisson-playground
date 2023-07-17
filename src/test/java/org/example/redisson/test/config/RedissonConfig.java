package org.example.redisson.test.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

import java.util.Objects;

public class RedissonConfig {
    private RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        if (Objects.isNull(this.redissonClient)) {
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://127.0.0.1:6379");
            redissonClient = Redisson.create(config);
        }
        return redissonClient;
    }

    public RedissonReactiveClient getReactiveClient() {
        return getRedissonClient().reactive();
    }
}
