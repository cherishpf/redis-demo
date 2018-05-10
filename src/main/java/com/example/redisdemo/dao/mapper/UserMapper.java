package com.example.redisdemo.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.desktop.utils.mybatis.SimpleSelectLangDriver;
import com.example.redisdemo.domain.User;

/**
 * @author yangpengfei
 * @date 2018年5月8日
 */
@Mapper
public interface UserMapper {
	@Select("select * from pkpm_cloud_user_info (#{user})")
    @Lang(SimpleSelectLangDriver.class)
    List<User> select(User user);

}
