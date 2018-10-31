package com.dy.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisMovedDataException;
import redis.clients.util.JedisClusterCRC16;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Cluster集群后的redis客户端操作demo
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/10/8 11:10
 */
public class ClusterTest {

    public static void main(String[] args){

        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("192.168.40.129", 6380));
        jedisClusterNode.add(new HostAndPort("192.168.40.129", 6379));
        jedisClusterNode.add(new HostAndPort("192.168.40.129", 6383));

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNode, 1000, 1000, 5, poolConfig);

        jedisCluster.set("jedisClusterHello", "hello myCluster");
        System.out.println(jedisCluster.get("c"));
        //jedisCluster.set("{user}:a", "userA");
        //jedisCluster.set("{user}:b", "userB");
        //System.out.println(JedisClusterCRC16.getSlot("{user}:a") == JedisClusterCRC16.getSlot("{user}:b"));
        //List<String> list = jedisCluster.mget("{user}:a", "{user}:b");  //同一个slot的数据支持mget mset等批量操作



        Set<Entry<String, JedisPool>> set = jedisCluster.getClusterNodes().entrySet();
        for (Entry<String ,JedisPool> entry : set){
            Jedis jedis = entry.getValue().getResource();

            if (!isMaster(jedis)){
                //continue;
            }

            String str = jedis.info("clients");
            System.out.println(str);

            try {
                System.out.println(jedis.get("c"));
            } catch (JedisMovedDataException e){
                System.out.println(e.getMessage());
                String moveStr = e.getMessage().split(" ")[2];

                Jedis newJedis = new Jedis(moveStr.split(":")[0], Integer.parseInt(moveStr.split(":")[1]));
                System.out.println("newJedis get c : " + newJedis.get("c"));
                newJedis.close();
            }
            System.out.println("\n === end \n");
        }

        return;
    }




    public static boolean isMaster(Jedis jedis){
        String[] data = jedis.info("Replication").split("\r\n");
        for (String line : data){
            if ("role:master".equals(line.trim())){
                return true;
            }
        }

        return false;
    }
}
