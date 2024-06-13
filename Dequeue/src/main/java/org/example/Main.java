package org.example;

import redis.clients.jedis.Jedis;

public class Main {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost",6379);
        for(int i=0;i<=10;i++){
            System.out.println(jedis.rpop("queue"));
        }
        System.out.println("Queue is Empty now");
    }
}