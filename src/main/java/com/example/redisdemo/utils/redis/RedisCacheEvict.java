package com.example.redisdemo.utils.redis;

import java.lang.annotation.*;

/**
 * 自定义注解RedisCacheEvict：用于缓存清除
 * 
 * @author yangpengfei
 * @date 2018年5月10日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RedisCacheEvict {
	// 缓存名称，可以多个
	String[] cacheNames() default "";

	// 缓存key
	String cacheKey();

	// 是否清空cacheName的全部数据
	boolean allEntries() default false;
}
