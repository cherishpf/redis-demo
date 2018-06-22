package com.example.redisdemo.utils.rediscluster;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration
public class JedisClusterConfig {
	@Resource
	private RedisProperties redisProperties;

	/**
	 * 注意： 这里返回的 JedisCluster 是单例的，并且可以直接注入到其他类中去使用
	 * 
	 * @return
	 */
	@Bean
	public JedisCluster getJedisCluster() {
		
		// 从配置文件中获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
		String[] serverArray = redisProperties.getClusterNodes().split(",");
		
		Set<HostAndPort> nodes = new HashSet<>();

		for (String ipPort : serverArray) {
			String[] ipPortPair = ipPort.split(":");
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
		}

		return new JedisCluster(nodes, 1000, 5, new GenericObjectPoolConfig());
		
		// 需要密码连接的创建对象方式
		// return new
		// JedisCluster(nodes,redisProperties.getCommandTimeout(),1000,1,redisProperties.getPassword()
		// ,new GenericObjectPoolConfig());
	}
}
