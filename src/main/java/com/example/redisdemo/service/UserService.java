package com.example.redisdemo.service;

import java.util.List;

import com.example.redisdemo.domain.User;

/**
 * @author yangpengfei
 * @date 2018年5月8日
 */
public interface UserService {

	/**
     * 查询用户列表
     *
     * @param info
     * @return
     */

	List<User> queryUserList(String userName);
	
	/**
     * 查询用户
     *
     * @param info
     * @return
     */

	User queryUser(Integer userID);
	
	/**
     * 查询用户名字
     *
     * @param info
     * @return
     */

	String queryUserName(Integer userID);
}
