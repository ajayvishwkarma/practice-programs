package com.demo2.service;

import com.demo2.entity.Employee;

public interface EmpService {
    public String addEmployee(Employee employee);

    public Employee getEmployee(String id);

    public Employee updateEmployee(String id,Employee employee);

    public String removeEmployee(String id);
}
