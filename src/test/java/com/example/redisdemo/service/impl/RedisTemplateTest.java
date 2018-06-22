package com.example.redisdemo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangpengfei
 * @date 2018年6月22日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTemplateTest {

	@Resource
	StringRedisTemplate stringRedisTemplate;
	
	// redis string
	@Test
	public void valueSetRedisTest(){
		stringRedisTemplate.opsForValue().set("key", "value1");
	}
	
	@Test
	public void valueGetRedisTest(){
	    String value = stringRedisTemplate.opsForValue().get("key");
	    log.info("value:{}", value);
	}

	@Test
	public void valueDelRedisTest(){
	    stringRedisTemplate.delete("key");
	}

	@Test
	public void valueTimeoutRedisTest(){
		// 设置过期时间为2分钟
	    stringRedisTemplate.opsForValue().set("timeStep", new Date().getTime()+"", 2 ,TimeUnit.MINUTES);
	}
	
	// redis list
	// list数据类型适合于消息队列的场景:比如12306并发量太高，而同一时间段内只能处理指定数量的数据！必须满足先进先出的原则，其余数据处于等待
	@Test
	public void listPushRedisTest(){
	    // leftPush依次由右边添加
	    stringRedisTemplate.opsForList().rightPush("myList","1");
	    stringRedisTemplate.opsForList().rightPush("myList","2");
	    stringRedisTemplate.opsForList().rightPush("myList", "A");
	    stringRedisTemplate.opsForList().rightPush("myList", "B");
	    // leftPush依次由左边添加
	    stringRedisTemplate.opsForList().leftPush("myList", "0");
	}

	@Test
	public void listGetListRedisTest(){
	    // 查询类别所有元素
	    List<String> listAll = stringRedisTemplate.opsForList().range( "myList", 0, -1);
	    log.info("list all {}", listAll);
	    // 查询前4个元素
	    List<String> list = stringRedisTemplate.opsForList().range( "myList", 0, 3);
	    log.info("list limit {}", list);
	}

	@Test
	public void listRemoveOneRedisTest(){
	    // 删除先进入的B元素
	    stringRedisTemplate.opsForList().remove("myList",1, "B");
	}

	@Test
	public void listRemoveAllRedisTest(){
	    // 删除所有A元素
	    stringRedisTemplate.opsForList().remove("myList",0, "A");
	}
	
	// Redis hash
	@Test
	public void hashPutRedisTest(){
	    // map的key值相同，后添加的覆盖原有的
	    stringRedisTemplate.opsForHash().put("banks:12600000", "a", "b");
	    stringRedisTemplate.opsForHash().put("banks:12600000", "a1", "b");
	    stringRedisTemplate.opsForHash().put("banks:12600000", "a2", "b2");
	}

	@Test
	public void hashGetEntiresRedisTest(){
	    // 获取map对象
	    Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("banks:12600000");
	    log.info("objects:{}", map);
	}

	@Test
	public void hashKeyDeleteRedisTest(){
	    // 根据map的key删除这个元素
	    stringRedisTemplate.opsForHash().delete("banks:12600000", "a2");
	}

	@Test
	public void hashGetKeysRedisTest(){
	    // 获得map的key集合
	    Set<Object> objects =  stringRedisTemplate.opsForHash().keys("banks:12600000");
	    log.info("objects:{}", objects);
	}

	@Test
	public void hashGetValueListRedisTest(){
	    // 获得map的value列表
	    List<Object> objects = stringRedisTemplate.opsForHash().values("banks:12600000");
	    log.info("objects:{}", objects);
	}

	@Test
	public void hashSize() {
	    // 获取map对象大小
	    long size =  stringRedisTemplate.opsForHash().size("banks:12600000");
	    log.info("size:{}", size);
	}
}
