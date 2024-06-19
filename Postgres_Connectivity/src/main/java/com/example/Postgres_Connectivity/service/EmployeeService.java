package com.example.Postgres_Connectivity.service;

import com.example.Postgres_Connectivity.api.model.Employee;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private List<Employee> employeeList;

    public EmployeeService(){
        employeeList=new ArrayList<>();

    }
    public List<Employee> getEmployees() {
        String jdbcURL="jdbc:postgresql://localhost:5432/test";
        String userName="postgres", password="password";
        try{
            Class.forName("org.postgresql.Driver");
            Connection conn= DriverManager.getConnection(jdbcURL,userName,password);
            System.out.println(conn.isClosed());
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("select * from employee");
            while(rs.next()){
                Employee employee=new Employee();
                employee.setName(rs.getString("name"));
                employee.setDesignation(rs.getString("designation"));
                employeeList.add(employee);
            }
            String output=new GsonBuilder().setPrettyPrinting().create().toJson(employeeList);
            System.out.println(output);
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return employeeList;
    }
}
