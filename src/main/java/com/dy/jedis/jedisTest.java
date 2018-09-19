package com.dy.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * 类的描述
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/9/14 16:30
 */
public class jedisTest {

    public static void main(String[] args){
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 5);  //设置最大连接数
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 3);    //设置最大空闲数
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);    //设置最小空闲数
        poolConfig.setJmxEnabled(true);
        poolConfig.setMaxWaitMillis(10000); //最大等待时间10s
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.40.129", 6380);

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get("win7");
            System.out.println(value);

            // pipeline demo
            Pipeline pipeline = jedis.pipelined();
            pipeline.set("hello", "pipeline World");
            pipeline.incr("number");
            pipeline.sync();    //不需要返回结果的pipeline调用

            // lua demo
            String[] params = {"number", "hello", "57", "luaTest world"};
            String luaScript = "return redis.call('mset', KEYS[1], ARGV[1], KEYS[2], ARGV[2])";
            Object result = jedis.eval(luaScript, 2, params);
            System.out.println(result);


        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
    }
}
