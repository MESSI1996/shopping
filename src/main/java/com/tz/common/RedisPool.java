package com.tz.common;

import com.tz.utils.PropertiesUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
 */
public class RedisPool {

    private  static JedisPool pool;
    //最大连接数
    private static Integer maxTotal=Integer.parseInt(PropertiesUtils.readKey("redis.max.total"));
   //最大空弦数
    private static  Integer maxIdle=Integer.parseInt(PropertiesUtils.readKey("redis.max.idle"));
    //最小空闲数
    private static  Integer minIdle=Integer.parseInt(PropertiesUtils.readKey("redis.min.idle"));
    //在获取jedis实例时判断该实例是否有效
    private static  boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtils.readKey("redis.test.borrow"));
    //把jedis实例放回连接池时判断它是否有效
    private  static  boolean testOnReturn=Boolean.parseBoolean(PropertiesUtils.readKey("redis.test.return"));
    private  static  String redisIp=PropertiesUtils.readKey("redis.ip");
    private  static  Integer redisPort=Integer.parseInt(PropertiesUtils.readKey("redis.port"));



    private static  void  initpool(){
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);


         pool=new JedisPool(config,redisIp,redisPort,10000*2);
    }

    static {
        initpool();
    }

    public static Jedis getJedis(){
        return  pool.getResource();
    }

    public static  void returnSource(Jedis jedis){
        pool.returnResource(jedis);
    }

    public static  void  returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static  void main(String args[]){
            Jedis jedis=getJedis();
            jedis.set("username","123");;
            returnSource(jedis);
            pool.destroy();
            System.out.println("程序结束");
    }

}
