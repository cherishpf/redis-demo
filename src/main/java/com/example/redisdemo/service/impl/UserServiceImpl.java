package com.example.redisdemo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redisdemo.dao.UserDAO;
import com.example.redisdemo.domain.User;
import com.example.redisdemo.service.UserService;
import com.example.redisdemo.utils.redis.RedisCacheable;
import com.pkpmdesktopcloud.redis.RedisCache;

/**
 * @author yangpengfei
 * @date 2018年5月8日
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final String USER_SAME_NAME = "userSameName";
	@Resource
    private UserDAO userDAO;
    
	@SuppressWarnings("unchecked")
	@Override
	public List<User> queryUserList(String userName) {
		RedisCache cache = new RedisCache(USER_SAME_NAME);// (加载redis.properties中的redis连接信息)
		// 查询redis缓存
		List<User> userList = (List<User>) cache.getObject(userName);
		// 若存在Redis缓存，从缓存中读取
		if(userList != null) {
			return userList;
		}
		// 若不存在对应的Redis缓存，从数据库查询
		User user = new User();
		user.setUserName(userName);
		userList = userDAO.select(user);
		// 写入Redis缓存
		cache.putObject(userName, userList);
		cache.setTimeOut(36000);
		return userList;
	}
	
	
//	// 使用自定义缓存注解(加载application.properties中的redis连接信息)
//	@Override
//	@RedisCacheable(cacheNames = {"UserList"}, cacheKey = "#userName", expire = 3600)
//	public List<User> queryUserList(String userName) {
//		User user = new User();
//		user.setUserName(userName);
//		List<User> userList = userDAO.select(user);
//		return userList;
//	}

	@Override
	@RedisCacheable(cacheKey = "#userID", expire = 3600)
	public User queryUser(Integer userID) {
		return userDAO.selectById(userID);
	}

}
