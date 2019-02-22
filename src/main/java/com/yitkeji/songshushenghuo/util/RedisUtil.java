package com.yitkeji.songshushenghuo.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisUtil {

    private static RedisTemplate redisTemplate = null;

    public static final void setRedisTemplate(RedisTemplate rd){
        redisTemplate = rd;

    }

    public static final <T> T get(String cacheKey){
        Object cache = redisTemplate.opsForValue().get(cacheKey);
        if(cache == null){
            return null;
        }
        return (T)cache;
    }

    public static final <T> void set(String cacheKey, T object){
        redisTemplate.opsForValue().set(cacheKey, object);
    }

    public static final <T> void set(String cacheKey, T object, long time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(cacheKey, object, time, timeUnit);
    }

    public static final <T> T expired(String cacheKey){
        T object = get(cacheKey);
        set(cacheKey, null, 1, TimeUnit.MILLISECONDS);
        return object;
    }

    public static final <T> Set<T> keys(String pattern){
        return redisTemplate.keys(pattern);
    }
}
