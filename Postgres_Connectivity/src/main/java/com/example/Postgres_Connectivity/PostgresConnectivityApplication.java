package com.example.Postgres_Connectivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class PostgresConnectivityApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostgresConnectivityApplication.class, args);

		Map<String,String> employee=new HashMap();
		String jdbcURL="jdbc:postgresql://localhost:5432/test";
		String userName="postgres", password="password";
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn= DriverManager.getConnection(jdbcURL,userName,password);
			System.out.println(conn.isClosed());
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select * from employee");
			List<Map<String,String>> list=new ArrayList<>();
			while(rs.next()){
				Map<String,String> map=new HashMap<>();
				map.put("name",rs.getString("name"));
				map.put("designation",rs.getString("designation"));
				list.add(map);
			}
			System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(list));
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

	}

}
