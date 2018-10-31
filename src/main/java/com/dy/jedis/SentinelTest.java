package com.dy.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Redis Sentinel 相关DEMO
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/9/28 11:34
 */
public class SentinelTest {


    public static void main(String[] args){

        JedisService jedisService = new JedisService();

        int timeout = 20;   //10 * 5s = 50s
        try {
            while (timeout-- > 0) {
                try {

                    System.out.println(jedisService.configGet("port"));
                    System.out.println(jedisService.getDbSize());
                    System.out.println("key:hello , value:" + jedisService.get("hello"));
                    System.out.println();

                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e){
            System.out.println("\n=========================\n");
            e.printStackTrace();
        }
    }
}

class JedisService{

    JedisSentinelPool jedisSentinelPool = null;

    public JedisService(){
        Set<String> sentinelSet = new HashSet<String>();
        sentinelSet.add("192.168.40.129:26379");
        sentinelSet.add("192.168.40.129:26380");
        sentinelSet.add("192.168.40.129:26381");

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxWaitMillis(10);
        poolConfig.setJmxEnabled(true);

        jedisSentinelPool = new JedisSentinelPool("mymaster",
                sentinelSet, poolConfig);
    }

    public Long getDbSize(){

        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();

            return jedis.dbSize();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
        return null;
    }

    public String get(String key){
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();

            return jedis.get(key);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
        return null;
    }

    public List<String> configGet(String profix){
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();

            return jedis.configGet(profix);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
        return null;
    }

}