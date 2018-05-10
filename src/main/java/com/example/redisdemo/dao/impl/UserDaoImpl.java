package com.example.redisdemo.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.example.redisdemo.dao.UserDAO;
import com.example.redisdemo.dao.mapper.UserMapper;
import com.example.redisdemo.domain.User;

/**
 * @author yangpengfei
 * @date 2018年5月8日
 */
@Repository
public class UserDaoImpl implements UserDAO {

	@Resource
    private UserMapper mapper;
    
	@Override
	public List<User> select(User user) {
		return mapper.select(user);
	}

	@Override
	public User selectById(Integer userID) {
		User user = new User();
		user.setUserId(userID);
		List<User> list = mapper.select(user);
        if (list != null && list.size() > 0)
            return list.get(0);
		return null;
	}

}
