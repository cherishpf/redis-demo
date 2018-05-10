package com.example.redisdemo.utils.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.example.redisdemo.utils.serialize.JDKSerializeUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义缓存注解AOP实现
 * 
 * @author yangpengfei
 * @date 2018年5月10日
 */
@Aspect
@Component
@Slf4j
public class RedisCacheableAspect {
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private DefaultKeyGenerator defaultKeyGenerator;

	/**
	 * * @Description： 读取缓存数据 * @param： * @return：
	 */
	@Around(value = "@annotation(cache)")
	public Object cached(final ProceedingJoinPoint pjp, RedisCacheable cache) throws Throwable {
		try {
			// 生成缓存KEY
			String[] keys = defaultKeyGenerator.generateKey(pjp, cache.cacheNames(), cache.cacheKey());
			Object valueData = null;
			for (String key : keys) {
				// 获取缓存中的值
				ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
				byte[] value = (byte[]) valueOper.get(key);
				if (value != null) {
					// 如果缓存有值，需要判断刷新缓存设置和当前缓存的失效时间
					int reflash = cache.reflash();
					if (reflash > 0) {
						// 查询当前缓存失效时间是否在主动刷新规则范围内
						long exp = redisTemplate.getExpire(key, TimeUnit.SECONDS);
						if (exp <= reflash) {
							// 主动刷新缓存，为不影响本次获取效率，采用异步线程刷新缓存
						}
					}
					return JDKSerializeUtil.unserialize(value);
				}
				// 缓存中没有值，执行实际数据查询方法
				if (valueData == null)
					valueData = pjp.proceed();
				// 写入缓存
				if (cache.expire() > 0) {
					valueOper.set(key, JDKSerializeUtil.serialize(valueData), cache.expire(), TimeUnit.SECONDS);
					// 否则设置缓存时间 ,序列化存储
				} else {
					valueOper.set(key, JDKSerializeUtil.serialize(valueData));
				}
			}
			return valueData;
		} catch (Exception e) {
			log.error("读取Redis缓存失败，异常信息：" + e.getMessage());
			return pjp.proceed();
		}
	}

	/**
	 * * @Description： 新增缓存 * @param： * @return： *
	 */
	@Around(value = "@annotation(cacheput)")
	public Object cachePut(final ProceedingJoinPoint pjp, RedisCachePut cacheput) throws Throwable {
		try {
			// 生成缓存KEY
			String[] keys = defaultKeyGenerator.generateKey(pjp, cacheput.cacheNames(), cacheput.cacheKey());
			Object valueData = pjp.proceed();
			// 写入缓存
			for (String key : keys) {
				ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
				if (cacheput.expire() > 0) {
					valueOper.set(key, JDKSerializeUtil.serialize(pjp.getArgs()[0]), cacheput.expire(),
							TimeUnit.SECONDS);
				} else {
					valueOper.set(key, JDKSerializeUtil.serialize(pjp.getArgs()[0]));
				}
			}
			return valueData;
		} catch (Exception e) {
			log.error("写入Redis缓存失败，异常信息：" + e.getMessage());
			return pjp.proceed();
		}
	}

	/**
	 * @Description： 删除缓存
	 * 
	 * @param
	 * @return
	 */
	@Around(value = "@annotation(cachevict)")
	public Object cacheEvict(final ProceedingJoinPoint pjp, RedisCacheEvict cachevict) throws Throwable {
		try {
			String[] cacheNames = cachevict.cacheNames();
			boolean allEntries = cachevict.allEntries();
			if (allEntries) {
				if (cacheNames == null || cacheNames.length == 0) {
					Signature signature = pjp.getSignature();
					cacheNames = new String[] { signature.getDeclaringTypeName() + "." + signature.getName() };
				}
				for (String cacheName : cacheNames) {
					redisTemplate.delete(redisTemplate.keys("*" + "RedisKey_CacheName_" + cacheName + "*"));
				}
			} else {
				String[] keys = defaultKeyGenerator.generateKey(pjp, cachevict.cacheNames(), cachevict.cacheKey());
				for (String key : keys)
					redisTemplate.delete(key);
			}
		} catch (Exception e) {
			log.error("删除Redis缓存失败，异常信息：" + e.getMessage());
		}
		return pjp.proceed();
	}
}
