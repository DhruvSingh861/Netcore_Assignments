package com.example.Redis_Connectivity;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RedisConnectivityApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisConnectivityApplication.class, args);

		Jedis jedis=new Jedis();
		Map<String,String> employee=new HashMap();
		employee.put("name",jedis.hget("Employee","name"));
		employee.put("city",jedis.hget("Employee","city"));
		System.out.println(new Gson().toJson(employee));
	}

}