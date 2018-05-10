package com.example.redisdemo.utils.redis;

import java.lang.annotation.*;

/**
 * 自定义注解RedisCachePut：用于缓存写入
 * 
 * @author yangpengfei
 * @date 2018年5月10日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RedisCachePut {
	// 缓存名称，可以多个
	String[] cacheNames() default "";

	// 缓存key
	String cacheKey();

	// 有效期时间（单位：秒）,默认8个小时
	int expire() default 28800;
}
