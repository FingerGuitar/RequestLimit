package com.finger.limit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtils {
 
 
    @Autowired
 
    private RedisTemplate redisTemplate;
 
 
    /**
     * 写入String型 [ 键，值]
     *
     * @param key
     * @param value
     * @return
     */
 
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
 
    }
 
 
 
  /**
     * 写入String型,顺便带有过期时间 [ 键，值]
     *
     * @param key
     * @param value
     * @return
     */
 
    public boolean setWithTime(final String key, Object value,int seconds) {
        boolean result = false;
        try {
 
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value,seconds, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
 
    }
 
 
 
    /**
     * 批量删除对应的value
     *
     * @param keys
     */
 
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
 
    /**
     * 批量删除key
     *
     * @param pattern
     */
 
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }
 
    /**
     * 删除对应的value
     *
     * @param key
     */
 
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
 
 
    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
 
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
 
 
    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
 
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }
 
 
    /**
     * 哈希 添加
     * hash 一个键值(key->value)对集合
     *
     * @param key
     * @param hashKey
     * @param value
     */
 
    public void hmSet(String key, Object hashKey, Object value) {
 
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
 
        hash.put(key, hashKey, value);
 
    }
 
 
    /**
     * Hash获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
 
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
 
    }
 
 
    /**
     * 列表添加
     * list:lpush key value1
     *
     * @param k
     * @param v
     */
 
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }
 
 
    /**
     * 列表List获取
     * lrange： key 0 10 (读取的个数 从0开始 读取到下标为10 的数据)
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
 
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }
 
 
    /**
     * Set集合添加
     *
     * @param key
     * @param value
     */
 
    public void add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }
 
 
    /**
     * Set 集合获取
     *
     * @param key
     * @return
     */
 
    public Set<Object> setMembers(String key) {
 
        SetOperations<String, Object> set = redisTemplate.opsForSet();
 
        return set.members(key);
 
    }
 
 
    /**
     * Sorted set :有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
 
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }
 
 
    /**
     * Sorted set:有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
 
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
 
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
 
        return zset.rangeByScore(key, scoure, scoure1);
 
    }
 
 
    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
 
    public Set<Integer> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
 
    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
 
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 
}