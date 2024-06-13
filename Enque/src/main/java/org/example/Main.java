package org.example;

import redis.clients.jedis.Jedis;

public class Main {
    public static void main(String[] args) {
        //simple redis list as a queue
        Jedis jedis=new Jedis("localhost",6379);
        for(int i=0;i<=10;i++){
            jedis.lpush("queue","Data:"+i);
            jedis.lpush("reliableQueue","Data:"+i);
        }

        System.out.println("10 entries is Enqued in 'queue' and 'reliableQueue'.");
    }
}