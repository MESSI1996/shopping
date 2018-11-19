package com.tz.utils;

import com.tz.common.RedisPool;
import redis.clients.jedis.Jedis;

/**
 *redis常用api封装类
 */
public class RedisPoolUtils {
    /*
    * 添加键值对
    * */
    public static  String set(String key,String valiue){
        Jedis jedis=null;
        String resullt=null;
        try{
            jedis= RedisPool.getJedis();
            jedis.set(key,valiue);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnSource(jedis);
        }

        return  resullt;
    }

    /*
    * 设置过期时间的键值对
    * */
    public static  String setex(String key,String valiue,int expireTime){
        Jedis jedis=null;
        String resullt=null;
        try{
            jedis= RedisPool.getJedis();
            jedis.setex(key,expireTime,valiue);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnSource(jedis);
        }

        return  resullt;
    }



    /*
    * 根据key获取value
    * */
    public static  String get(String key){
        Jedis jedis=null;
        String resullt=null;
        try{
            jedis= RedisPool.getJedis();
            jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnSource(jedis);
        }

        return  resullt;
    }


    /*
    * 根据key删除
    *
    * */

    public static  Long del(String key){
        Jedis jedis=null;
        Long resullt=null;
        try{
            jedis= RedisPool.getJedis();
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnSource(jedis);
        }

        return  resullt;
    }

    /*
    * 设置key的有效时间
    *
    * */
    public static  Long set(String key,int expireTime){
        Jedis jedis=null;
        Long resullt=null;
        try{
            jedis= RedisPool.getJedis();
            jedis.expire(key,expireTime);
        }catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnSource(jedis);
        }

        return  resullt;
    }

    public  static  void main(String args[]){
        setex("user","user",10);
    }

}

