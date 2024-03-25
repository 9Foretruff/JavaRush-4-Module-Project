package com.javarush.domain.redis.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class RedisUtil {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    @Getter
    private static final RedisClient redisClient;
    private static final StatefulRedisConnection<String, String> connection;

    static {
        redisClient = RedisClient.create("redis://" + REDIS_HOST + ":" + REDIS_PORT);
        connection = redisClient.connect();
    }

    public static RedisCommands<String, String> getRedisCommands() {
        return connection.sync();
    }

    public static void close() {
        connection.close();
        redisClient.shutdown();
    }

}