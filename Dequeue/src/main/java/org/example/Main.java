package org.example;

import redis.clients.jedis.Jedis;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost",6379);

        //removing from simple queue.
        for(int i=0;i<=10;i++){
            System.out.println(jedis.rpop("queue"));
        }

        //Reliable queue
        // a task is not removed from the queue immediately when it is dequeued.
        // Instead, it is moved to a temporary queue where it is stored until
        // the consumer confirms that the task has been processed
        for(int i=0;i<=10;i++){
            //RPOPLPUSH source destination
            jedis.rpoplpush("reliableQueue","tempQueue");
        }

        System.out.println("queue and reliableQueue is Empty now");
        System.out.println("Data is still present in the tempQueue. ->");
        List<String> l=jedis.lrange("tempQueue",0,10);
        System.out.println(l);
    }
}