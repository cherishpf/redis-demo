package com.example.redisdemo.utils.redis;

import java.lang.annotation.*;

/**
 * 自定义注解RedisCacheable：用于缓存读取
 * 
 * @author yangpengfei
 * @date 2018年5月10日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RedisCacheable {

	// 缓存名称，可以多个
	String[] cacheNames() default "";

	// 缓存key
	String cacheKey();

	// 有效期时间（单位：秒）,默认8个小时
	int expire() default 28800;

	// 缓存主动刷新时间（单位：秒），默认不主动刷新
	int reflash() default -1;
}
