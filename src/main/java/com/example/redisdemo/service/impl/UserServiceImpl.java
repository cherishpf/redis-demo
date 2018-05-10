package com.example.redisdemo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
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
	@Resource
	private StringRedisTemplate stringRedisTemplate;
    
	// 使用StringRedisTemplate，key、value使用string存储
	@Override
	public String queryUserName(Integer userID) {
		String usrName = stringRedisTemplate.opsForValue().get(userID.toString());
		// 若存在Redis缓存，从缓存中读取
		if (StringUtils.isNotBlank(usrName)) {
			return usrName;
		} else {
			// 若不存在对应的Redis缓存，从数据库查询
			User user = userDAO.selectById(userID);
			usrName = user.getUserName();
			// 写入Redis缓存
			stringRedisTemplate.opsForValue().set(userID.toString(), usrName);
			return usrName;
		}
	}
		
		
	// 使用RedisCache工具类，hash
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
		// 写入Redis缓存，如果需要设置过期时间，先设置过期时间，后执行写入方法
//		cache.setTimeOut(7200);
		cache.putObject(userName, userList);
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

	
	// 使用自定义缓存注解(加载application.properties中的redis连接信息)
	// expire过期时间（秒），-1永不过期
	// redis缓存  key以String数据类型存储，value以序列化的数据存储
	@Override
	@RedisCacheable(cacheKey = "#userID", expire = 3600)
	public User queryUser(Integer userID) {
		return userDAO.selectById(userID);
	}

}
