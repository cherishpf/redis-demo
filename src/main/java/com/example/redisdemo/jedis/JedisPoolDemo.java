package com.example.redisdemo.jedis;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis连接池demo
 * @author yangpengfei
 * @date 2018年5月8日
 */
@Slf4j
public class JedisPoolDemo {
	
    public static void main(String[] args) {
    	
        // 构建连接池配置信息
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置最大连接数
        jedisPoolConfig.setMaxTotal(50);

        // 构建连接池
        // String host, int port, int timeout, String password
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "139.159.254.147", 26379, 0, "Broad_@redis");

        String uuid = UUID.randomUUID().toString();
        log.info("jedisPool uuid : " + uuid);
        // 从连接池中获取连接
        Jedis jedis = jedisPool.getResource();
	    // 选择对应的数据库
	    jedis.select(2); 
	    // 写入数据
        jedis.setex(uuid, 1000, "test jedis pool");
        // 读取数据
        System.out.println(jedis.get(uuid));

//        /* 
//         * hashset 
//         * 对于某些不定项操作可以利用哈希扩展 
//         */  
//        String userKey="userTest";  
//        jedis.hset(userKey, "name", "jim");  
//        jedis.hset(userKey, "age", "12");  
//        jedis.hset(userKey, "phone", "12345678901");  
//        System.out.println("12" + jedis.hget(userKey,"name"));  
//        System.out.println("13" + jedis.hgetAll(userKey));  
//        jedis.hdel(userKey,"phone");//删除  
//        System.out.println("14" + jedis.hgetAll(userKey));  
//        System.out.println("15" + jedis.hkeys(userKey));//获取所有key  
//        System.out.println("16" + jedis.hvals(userKey));//获取所有values  
//        System.out.println(jedis.hexists(userKey, "email"));//是否存在  
//        System.out.println(jedis.hexists(userKey, "age"));  
//        jedis.hsetnx(userKey, "school", "123");//不存在字段 添加，存在不改变  
//        jedis.hsetnx(userKey, "name", "Ben");  
//        System.out.println("19" + jedis.hgetAll(userKey));  
        
        // 释放连接池
        jedisPool.close();

    }

}
