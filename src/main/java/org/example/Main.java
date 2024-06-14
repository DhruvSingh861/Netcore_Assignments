package org.example;

import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Long s=System.currentTimeMillis();
        List<Employee> list=new ArrayList<>();
        for(int i=0;i<10000000;i++){
            Employee employee=new Employee();
            employee.setName("Dhruv");
            employee.setDesignation("ASE"+i);
            list.add(employee);
        }
        PgBulkInsert<Employee> bulkInsert=new PgBulkInsert<>(new PersonMapping());
        String jdbcURL="jdbc:postgresql://localhost:5432/test";
        String userName="postgres", password="password";
        Class.forName("org.postgresql.Driver");
        Connection conn= DriverManager.getConnection(jdbcURL,userName,password);
        bulkInsert.saveAll(PostgreSqlUtils.getPGConnection(conn),list.stream());


        System.out.println("Hello world!");
        System.out.println(conn.isClosed());

        conn.close();
        Long e=System.currentTimeMillis();
        System.out.println(((double)(e-s)/1000)+" sec");
        System.out.println("done");
    }
}