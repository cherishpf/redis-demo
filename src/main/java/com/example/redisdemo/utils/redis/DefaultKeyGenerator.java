package com.example.redisdemo.utils.redis;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * key生成策略，缓存名+缓存KEY（支持Spring EL表达式）
 * 
 * @author yangpengfei
 * @date 2018年5月10日
 */
@Component
@Slf4j
public class DefaultKeyGenerator {
	/**
	 * @Description： redis key生成 
	 * @param： cacheKey：key值必传，cacheNames：缓存名称，不传取方法路径
	 * @return：
	 */
	public String[] generateKey(ProceedingJoinPoint pjp, String[] cacheNames, String cacheKey)
			throws NoSuchMethodException {
		if (StringUtils.isEmpty(cacheKey))
			throw new NullPointerException("CacheKey can not be null...");
		Signature signature = pjp.getSignature();
		if (cacheNames == null || cacheNames.length == 0) {
			cacheNames = new String[] { signature.getDeclaringTypeName() + "." + signature.getName() };
		}
		String[] results = new String[cacheNames.length];
		// 解析cacheKey
		EvaluationContext evaluationContext = new StandardEvaluationContext();
		if (!(signature instanceof MethodSignature)) {
			throw new IllegalArgumentException("This annotation can only be used for methods...");
		}
		MethodSignature methodSignature = (MethodSignature) signature;
		// method参数列表
		String[] parameterNames = methodSignature.getParameterNames();
		Object[] args = pjp.getArgs();
		for (int i = 0; i < parameterNames.length; i++) {
			String parameterName = parameterNames[i];
			evaluationContext.setVariable(parameterName, args[i]);
		}
		for (int j = 0; j < cacheNames.length; j++) {
			results[j] = "RedisKey_CacheName_" + cacheNames[j] + "_CacheKey_"
					+ new SpelExpressionParser().parseExpression(cacheKey).getValue(evaluationContext, String.class);
			// 暂时只使用String类型
		}
		log.info("=============>>>generateKeys : " + Arrays.toString(results));
		return results;
	}
}