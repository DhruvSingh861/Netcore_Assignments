package org.example;

import redis.clients.jedis.Jedis;

public class Main {
    public static void main(String[] args) {
        Jedis jedis=new Jedis();
        System.out.println(jedis.get("name"));
    }
}