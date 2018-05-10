package com.example.redisdemo.dao;

import java.util.List;

import com.example.redisdemo.domain.User;

/**
 * @author yangpengfei
 * @date 2018年5月8日
 */
public interface UserDAO {
	/**
	 * 查询用户
	 * @param user
	 * @return
	 */
	List<User> select(User user);
	
	/**
	 * 查询用户
	 * @param user
	 * @return
	 */
	User selectById(Integer userID);
}
