package com.javarush.domain.redis.util;

import redis.clients.jedis.Jedis;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@UtilityClass
@Slf4j
public class RedisUtil {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final int MAX_TOTAL = 20;
    private static final int MAX_IDLE = 10;
    private static final int MIN_IDLE = 5;

    private static JedisPool jedisPool;

    public static Jedis getJedis() {
        if (jedisPool == null) {
            initJedisPool();
        }
        return jedisPool.getResource();
    }

    private static void initJedisPool() {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(MAX_TOTAL);
            poolConfig.setMaxIdle(MAX_IDLE);
            poolConfig.setMinIdle(MIN_IDLE);

            jedisPool = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT);
            log.info("JedisPool initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize JedisPool", e);
            throw new RuntimeException("Failed to initialize JedisPool", e);
        }
    }

    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
