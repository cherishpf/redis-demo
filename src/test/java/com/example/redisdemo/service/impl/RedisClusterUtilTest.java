package com.example.redisdemo.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.redisdemo.utils.rediscluster.RedisClusterUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangpengfei
 * @date 2018年6月22日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisClusterUtilTest {

	@Resource
    private RedisClusterUtil redisClusterUtil;
	
	@Test
	public void valueSetRedisTest(){
		for (int i=0;i<10;i++){
			redisClusterUtil.set("cluster-key"+i, "cluster-value"+i);
			log.info("redisClusterUtil result:{}", redisClusterUtil.get("cluster-key"+i));
		}
	}
}
