package org.example;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {

        //connected to Redis running on localhost default port (TCP Port 6379).
        Jedis jedis=new Jedis("localhost",6379);
        jedis.set("name", "Dhruvvv");
        jedis.set("surname", "Singh");

        String cachedResponse = jedis.get("name");
        System.out.println(cachedResponse);

        System.out.println(jedis.del("address"));

        //pushing data into List
        jedis.lpush("address","316");
        jedis.rpush("address","Clerk Colony");
        jedis.rpush("address","Indore");
        jedis.rpush("address","MP");

        //poping data from list
        String houseNumber = jedis.lpop("address");
        String state = jedis.rpop("address");
        System.out.println("house number: "+houseNumber);
        System.out.println("state: "+state);
        System.out.println("Colony: "+ jedis.lindex("address",0));

        //puting data into Redis Hash Map
        HashMap<String,String> details=new HashMap<>();
        details.put("name","Dhruv");
        details.put("surname","Singh");
        details.put("Company","Netcore");
        details.put("role","ASE");
        jedis.hmset("Employe:1",details);

        //getting data from Hash
        System.out.println(jedis.hget("Employe:1","name"));
        System.out.println(jedis.hget("Employe:1","Company"));
        System.out.println(jedis.hget("Employe:1","role"));

        //redis Set
        jedis.sadd("set","java", "python", "java", "SQL");
        Iterator<String> it=jedis.smembers("set").iterator();
        System.out.printf("\nSet Elements->\n");
        while(it.hasNext()) System.out.println(it.next());
    }
}