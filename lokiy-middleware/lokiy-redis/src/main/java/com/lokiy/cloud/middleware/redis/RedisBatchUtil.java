package com.lokiy.cloud.middleware.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Lokiy
 * @date 2020/5/26 0026
 * @description: redis 批量操作
 */
@Component
public class RedisBatchUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 批量插入key
     * @param kvMap k-v数据
     */
    public void pipelineSet(Map<String, Object> kvMap){
        RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            kvMap.forEach((k,v) -> connection.set(k.getBytes(), valueSerializer.serialize(v)));
            return null;
        });
    }


    /**
     * 批量查询key
     * @param keys 键集合
     */
    public List<Object> pipelineGet(List<String> keys){
        RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
        return redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            keys.forEach(k -> valueSerializer.deserialize(connection.get(k.getBytes())));
            return null;
        });
    }


    /**
     * 批量插入 hset
     * @param kkvMap k-v set数据
     */
    public void piplineHset(Map<String, Map<String, Object>> kkvMap){
        RedisSerializer<Object> hashValueSerializer = (RedisSerializer<Object>) redisTemplate.getHashValueSerializer();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            kkvMap.forEach((k, v) -> v.forEach((hk, hv) -> connection.hSet(k.getBytes(), hk.getBytes(), hashValueSerializer.serialize(hv))));
            return null;
        });
    }

    /**
     * 批量获取 hget
     * @param khkMap key和item
     * @return 获取的数据对象
     */
    public List<Object> piplineHget(Map<String, String> khkMap){
        RedisSerializer<Object> hashValueSerializer = (RedisSerializer<Object>) redisTemplate.getHashValueSerializer();
        return redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            khkMap.forEach((k, hk) -> hashValueSerializer.deserialize(connection.hGet(k.getBytes(), hk.getBytes())));
            return null;
        });
    }


    /**
     * 批量获取值
     * @param keys 键
     * @return  获取的键对象
     */
    public List<Object> multiGet(List<String> keys){
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 批量插入值，不具有事务性
     * @param kvMap k-v集合
     */
    public boolean multiSet(Map<String, Object> kvMap){
        try {
            redisTemplate.opsForValue().multiSet(kvMap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取 h hkey 的数据
     * @param key 键
     * @param hkeys item集合
     * @return 批量获取的数据对象值
     */
    public List<Object> multiHGet(String key, List<Object> hkeys){
        return redisTemplate.opsForHash().multiGet(key, hkeys);
    }



}
