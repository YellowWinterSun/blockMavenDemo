package com.dy.jedis;

import com.dy.bean.Car;
import com.dy.util.KryoUtil;
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
        poolConfig.setMaxTotal(4/*GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 5*/);  //设置最大连接数
        poolConfig.setMaxIdle(4/*GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 3*/);    //设置最大空闲数
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);    //设置最小空闲数
        poolConfig.setJmxEnabled(true);
        poolConfig.setMaxWaitMillis(1000); //最大等待时间
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setTimeBetweenEvictionRunsMillis(10*1000);    //30s
        poolConfig.setNumTestsPerEvictionRun(-1);
        poolConfig.setMinEvictableIdleTimeMillis(5*1000L);
        poolConfig.setTestWhileIdle(true);

        //JedisPool jedisPool = new JedisPool(poolConfig, "192.168.40.130", 6380);
        final JedisPool jedisPool = new JedisPool(poolConfig, "172.28.1.53", 6379);

        Jedis jedis1 = null;
        Jedis jedis2 = null;
        Jedis jedis3 = null;
        Jedis jedis4 = null;
        Jedis jedis5 = null;
        try {
             jedis1 = jedisPool.getResource();
             System.out.println("---1 get redis success");
             jedis2 = jedisPool.getResource();
             System.out.println("---2 get redis success");
             jedis3 = jedisPool.getResource();
             System.out.println("---3 get redis success");
             jedis4 = jedisPool.getResource();
             System.out.println("---4 get redis success");
             jedis5 = jedisPool.getResource();
             System.out.println("---5 get redis success");
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            if (null != jedis1){
                jedis1.close();
            }
            if (null != jedis2){
                jedis2.close();
            }
            if (null != jedis3){
                jedis3.close();
            }if (null != jedis4){
                jedis4.close();
            }
            if (null != jedis5){
                jedis5.close();
            }

        }


        for (int i = 0; i < 1; i++) {
            //测试高并发
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long threadId = Thread.currentThread().getId();
                    //System.out.println("------启动线程：" + threadId);

                    for (int i = 0; i < 500; i++) {
                        //System.out.println("-----1");
                        Jedis jedis = null;
                        try {
                            jedis = jedisPool.getResource();
                            jedis.auth("123456");
                            jedis.set("key1", "set " + threadId + "," + i);

                            //System.out.println(jedis.get("key1") + "睡觉");
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            System.out.println("error1" + e.getMessage());
                        } finally {
                            if (null != jedis) {
                                jedis.close();
                            }
                        }
                    }


                }
            }).start();

            //测试高并发
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long threadId = Thread.currentThread().getId();
                    //System.out.println("------启动线程2：" + threadId);

                    for (int i = 0; i < 500; i++) {
                        //System.out.println("-----2");
                        Jedis jedis = null;
                        try {
                            jedis = jedisPool.getResource();
                            jedis.auth("123456");
                            jedis.set("key2", "set " + threadId + "," + i);
                            //System.out.println(jedis.get("key2"));
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            System.out.println("error2" + e.getMessage());
                        } finally {
                            if (null != jedis) {
                                jedis.close();
                            }
                        }
                    }


                }
            }).start();
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("123456");
            String value = jedis.get("win7");
            System.out.println(value);


            // pipeline demo
//            Pipeline pipeline = jedis.pipelined();
//            pipeline.set("hello", "pipeline World");
//            pipeline.get("number");
//            pipeline.incr("number");
            //pipeline.sync();    //不需要返回结果的pipeline调用
//            List<Object> listResponse = pipeline.syncAndReturnAll();
//            for (Object obj : listResponse){
//                System.out.println(obj.toString());
//            }
//            System.out.println("-------- end pipeline --------");
            /*
            // lua demo
            String[] params = {"number", "hello", "57", "luaTest world"};
            String luaScript = "return redis.call('mset', KEYS[1], ARGV[1], KEYS[2], ARGV[2])";
            Object result = jedis.eval(luaScript, 2, params);
            System.out.println(result);
            */

//            Car car = new Car();
//            car.setCarColor("red");
//            car.setCarNumber("DY");
//            car.setHighestSpeed(100);
//
//            jedis.set("car", KryoUtil.objectToByte(car));

//            Car car = (Car) KryoUtil.toObject(jedis.get("car"));
//            System.out.println(car.getCarColor());


        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (null != jedis){
                jedis.close();
            }
        }
        try {
            Thread.sleep(15 * 1000L);

            System.out.println("睡觉");
            System.out.println("活跃中" + jedisPool.getNumActive());
            System.out.println("闲置中" + jedisPool.getNumIdle());

            Thread.sleep(15 * 1000L);

            System.out.println("活跃中" + jedisPool.getNumActive());
            System.out.println("闲置中" + jedisPool.getNumIdle());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
