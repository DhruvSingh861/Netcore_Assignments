package org.example;

import redis.clients.jedis.Jedis;

public class Main {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost",6379);
        jedis.set("name","Dhruv");
        jedis.set("role","ASE");
        System.out.println(jedis.get("name")+" : "+jedis.get("role"));
    }
}