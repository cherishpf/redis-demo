package com.example.redisdemo.jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * java操作redis集群demo
 * @author yangpengfei
 * @date 2018年6月22日
 */
public class JedisClusterDemo {

    public static void main(String[] args) throws IOException {
    	// 初始化集合，用于存放多个主机和端口
    	Set<HostAndPort> hostAndPortSet = new HashSet<>();
    	// 创建多个主机和端口实例，并添加到集合中
    	hostAndPortSet.add(new HostAndPort("*.*.*.*", 9001));
    	hostAndPortSet.add(new HostAndPort("*.*.*.*", 9002));
    	hostAndPortSet.add(new HostAndPort("*.*.*.*", 9003));
    	hostAndPortSet.add(new HostAndPort("*.*.*.*", 9004));
    	hostAndPortSet.add(new HostAndPort("*.*.*.*", 9005));
    	hostAndPortSet.add(new HostAndPort("*.*.*.*", 9006));
    	
    	// 创建config
    	JedisPoolConfig poolConfig = new JedisPoolConfig();
    	
    	// 通过config创建集群实例
    	JedisCluster jedis = new JedisCluster(hostAndPortSet, poolConfig);
    	
    	// 向集群中添加键值对
    	for (int i = 0; i < 10; i++) {
    		jedis.set("key"+i, "value"+i);
    		System.out.println(jedis.get("key"+i));
        }
    	
    	// 向集群中添加键值对
//    	jedis.setnx("key1", "value1");
    	
    	// 获取集群中的键为key1的值
//    	System.out.println(jedis.get("key1"));

        // 关闭连接
    	jedis.close();

    }
}
