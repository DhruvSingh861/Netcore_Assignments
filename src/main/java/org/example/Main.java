package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        System.out.println("Hello world!");
        String jdbcURL="jdbc:postgresql://localhost:5432/test";
        String userName="postgres", password="password";
        Class.forName("org.postgresql.Driver");
        Connection conn= DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println(conn.isClosed());
        Long s=System.currentTimeMillis();
        PreparedStatement ps=conn.prepareStatement("insert into employee(name, designation) values(?,?)");
        for(int i=0;i<10000000;i++){
            ps.setString(1,"Dhruv");
            ps.setString(2,"ASE"+i);
            ps.addBatch();
            ps.clearParameters();
        }
        ps.executeBatch();

        Long e=System.currentTimeMillis();
        System.out.println(((double)(e-s)/1000)+" sec");
        System.out.println("done");
    }
}