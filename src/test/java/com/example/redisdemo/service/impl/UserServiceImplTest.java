package com.example.redisdemo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.redisdemo.domain.User;
import com.example.redisdemo.service.UserService;
import com.example.redisdemo.utils.redis.RedisCacheableAspect;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangpengfei
 * @date 2018年5月8日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceImplTest {

	@Resource
	private UserService userService;
	
	@Test
	public void testQueryUserList() {
//		List<User> userList = userService.queryUserList("ypf01");
//		log.info("第一次查询返回数据： " + userList);
//		
//		List<User> userList1 = userService.queryUserList("ypf01");
//		log.info("第二次查询返回数据： " + userList1);
		
		User user1 = userService.queryUser(131);
		log.info("第1次查询返回数据： " + user1);
		
		User user2 = userService.queryUser(131);
		log.info("第2次查询返回数据： " + user2);
		
//		String userName1 = userService.queryUserName(131);
//		log.info("第1次查询返回数据： " + userName1);
//		
//		String userName2 = userService.queryUserName(131);
//		log.info("第2次查询返回数据： " + userName2);
	}

}
