package org.example;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
            CompletableFuture<String> f=CompletableFuture.supplyAsync(()->{
                Jedis jedis=new Jedis("localhost",6379);
                String address=jedis.lrange("address",0,5).toString();
                System.out.println("Daemon Thread : "+Thread.currentThread().isDaemon());
                try{
                    BufferedWriter bw=new BufferedWriter(new FileWriter("Result.txt"));
                    bw.write(address);
                    bw.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                return "Done (address got stored in Result.txt file)";
            });
            try{
                System.out.println(f.get());
            }
            catch (ExecutionException e){
                e.printStackTrace();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
}