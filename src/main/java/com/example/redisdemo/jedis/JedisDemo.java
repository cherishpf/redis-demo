package com.example.redisdemo.jedis;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

/**
 * jedis简单demo示例
 * 
 * @author yangpengfei
 * @date 2018年5月8日
 */
public class JedisDemo {

	public static void main(String[] args) {
		// 构造jedis对象
		Jedis jedis = new Jedis("139.159.254.147", 26379);
		// 密码验证
		jedis.auth("Broad_@redis");
		// 选择对应的数据库
		jedis.select(2);

		// string
		// 向redis中添加数据
		jedis.set("mytest", "123");
		// 从redis中读取数据
		String value = jedis.get("mytest");
		System.out.println(value);

		// list
		String key1 = "list";
		String value1 = "layman";
		// 将一个或多个值 value 插入到列表 key 的表头
		jedis.lpush(key1, value1, value1 + "1", value1 + "2", value1 + "3");
		// 移除列表 key 的头元素
		jedis.lpop(key1);
		// 移除列表 key 的尾元素
		jedis.rpop(key1);

		// hash
		String key2 = "hash";
		String field = "test";
		String value2 = "hdgoew";
		jedis.hset(key2, field, value2);

		/*
		 * hash 对于某些不定项操作可以利用哈希扩展
		 */
		String userKey = "userTest";
		jedis.hset(userKey, "name", "jim");
		jedis.hset(userKey, "age", "12");
		jedis.hset(userKey, "phone", "12345678901");
		System.out.println("12" + jedis.hget(userKey, "name"));
		System.out.println("13" + jedis.hgetAll(userKey));

		// hash 一次性添加多个值到一个key中
		Map<String, String> map = new HashMap<String, String>();
		map.put(field + 0, value2 + 0);
		map.put(field + 1, value2 + 1);
		map.put(field + 2, value2 + 2);
		jedis.hmset(key2 + 1, map);

		// set
		jedis.sadd("article1", "qqq", "eee", "ttt");

		// zset
		String key3 = "mostUsedLanguages";
		// 键、score、value
		jedis.zadd(key3, 100, "Java");

		// zset 一次性添加多个值
		Map<String, Double> scoreMembers = new HashMap<String, Double>();
		scoreMembers.put("Python", 90d);
		scoreMembers.put("Javascript", 80d);
		jedis.zadd(key3, scoreMembers);

		// 关闭连接
		jedis.close();

	}

}
