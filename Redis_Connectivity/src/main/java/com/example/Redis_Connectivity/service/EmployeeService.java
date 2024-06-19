package com.example.Redis_Connectivity.service;

import com.example.Redis_Connectivity.api.model.Address;
import com.example.Redis_Connectivity.api.model.Employee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private List<Employee> employeeList;

    public EmployeeService(){
        employeeList=new ArrayList<>();

    }
    public List<Employee> getEmployees() {
        Jedis jedis=new Jedis("localhost",6379);
        for(int i=0;i<2;i++){
            Employee employee=new Employee();
            employee.setName(jedis.hget("employee"+i,"name"));
            Address address=new Address();
            address.setStreet(jedis.hget("employee"+i,"address.street"));
            address.setCity(jedis.hget("employee"+i,"address.city"));
            employee.setAddress(address);
            employeeList.add(employee);
        }
        return employeeList;
    }
}
