/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
 * OkStack is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan
 * PubL v2. You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 * /
 */

package org.okstar.platform.common.redis.service;

import io.quarkus.arc.impl.Sets;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 * 
 * 
 **/
@ApplicationScoped
public class RedisService
{
//    @Inject
//    public RedisTemplate redisTemplate;

    @Inject
    RedisDataSource redisDS;

//    ReactiveRedisDataSource reactiveRedisDS;
    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        ValueCommands<String, T> valueCommands = redisDS.value((Class<T>) value.getClass());
        valueCommands.set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key,
                                   final T value,
                                   final Long timeout,
                                   final TimeUnit timeUnit)
    {
        ValueCommands<String, T> valueCommands = redisDS.value((Class<T>) value.getClass());
        long seconds = timeUnit.toSeconds(timeout);
        valueCommands.setex(key, seconds, value);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {

        return false;
//        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key)
    {
        return redisDS.key().exists(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        ValueCommands<String, Object> value = redisDS.value(Object.class);
        Object o = value.get(key);
        return (T) o;
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
//        return redisTemplate.delete(key);
        return false;
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection)
    {
        return 0;
//        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        return 0;
//        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
//        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        return Collections.emptyList();
//        return redisTemplate.opsForList().range(key, 0, -1);
    }


    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return Sets.of();
//        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
//        if (dataMap != null) {
//            redisTemplate.opsForHash().putAll(key, dataMap);
//        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return new HashMap<>();
//        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
//        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        return null;
//        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
//        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        return List.of();
//        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     * 
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        return Collections.emptyList();
//        return redisTemplate.keys(pattern);
    }

    /**
     * 获取锁过期时间  1h
     */
    public static final int LOCK_EXPIRE = 60 * 60 * 1000;

    /**
     * 获取分布式锁
     *
     * @param key key值
     * @return 是否获取到
     */
    public synchronized boolean tryLock(String key) {

//        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
//
//            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
//            Boolean acquire = connection.setNX(key.getBytes(), String.valueOf(expireAt).getBytes());
//
//            if (acquire) {
//                return true;
//            } else {
//
//                byte[] value = connection.get(key.getBytes());
//
//                if (Objects.nonNull(value) && value.length > 0) {
//
//                    long expireTime = Long.parseLong(new String(value));
//
//                    if (expireTime < System.currentTimeMillis()) {
//                        // 如果锁已经过期
//                        byte[] oldValue = connection.getSet(key.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
//                        // 防止死锁
//                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
//                    }
//                }
//            }
//            return false;
//        });

        return true;
    }

    /**
     * 释放锁
     *
     * @param key key值
     * @return 是否获取到
     */
    public synchronized boolean releaseLock(String key) {
//        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
//            byte[] value = connection.get(key.getBytes());
//            //判断key是否存在，不存在说明已经删除
//            if (Objects.nonNull(value) && value.length > 0) {
//                long expireTime = Long.parseLong(new String(value));
//                //判断过期时间是否大于当前时间，如果过期时间没到，手动删除key
//                if (expireTime >= System.currentTimeMillis()) {
//                    connection.del(key.getBytes());
//                }
//            }
//            return true;
//        });
        return true;
    }

    /**
     * @param key:
     * @return java.lang.Long
     * @Description 获取过期时间
     * @Date 17:38 2019-12-19
     */
    public Long getExpire(String key) {
//        Long expire = redisTemplate.getExpire(key);
//        return expire;
        return 0L;
    }

    /**
     * 减一操作（原子性）
     *
     * @param key: Reids key
     * @return 操作后的结果
     * @date 2020/3/9 13:53
     **/
    public Long decrement(String key, int num) throws Exception {
//        Boolean result = redisTemplate.hasKey(key);
//        if (result == null || !result) {
//            throw new Exception("缓存数据不存在");
//        }
//        return redisTemplate.opsForValue().increment(key, -num);
        return 0L;
    }


    /**
     * 加一操作（原子性）
     *
     * @param key: Reids key
     * @return 操作后的结果
     * @date 2020/3/9 13:53
     **/
    public Long increment(String key, int num) throws Exception {
//        Boolean result = redisTemplate.hasKey(key);
//        if (result == null || !result) {
//            throw new Exception("缓存数据不存在");
//        }
//        return redisTemplate.opsForValue().increment(key, num);
        return 0L;
    }

    /**
     * 分布式锁
     *
     * @param key
     * @param value
     * @param expire 秒
     *
     * @return
     */
    public  Boolean setnx(String key, String value, int expire) {
//        return (Boolean)redisTemplate.execute((connection) -> {
//            try {
//                Boolean successFull = connection.setNX(key.getBytes("UTF-8"),value.getBytes("UTF-8"));
//                if (successFull) {
//                    redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//                }
//                return successFull;
//            } catch (Exception var8) {
//                var8.printStackTrace();
//                return null;
//            }
//        }, true);
        return false;
    }

}
