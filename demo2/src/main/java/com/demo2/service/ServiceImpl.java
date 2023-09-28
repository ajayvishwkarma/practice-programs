package com.demo2.service;

import com.demo2.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceImpl implements EmpService {

    public static Map<String, Employee> map = new HashMap<>();
    @Override
    public String addEmployee(Employee employee) {
        map.put(employee.getEmpId(),employee);
        return "employee added";
    }

    @Override
    public Employee getEmployee(String empId) {
       Employee emp=  map.get(empId);
       return emp;
    }

    @Override
    public Employee updateEmployee(String id, Employee employee) {
        Employee emp=  map.get(id);
        if(emp != null) {
            emp.setEmpName(employee.getEmpName() != null ? employee.getEmpName() : emp.getEmpName());
            emp.setEmpRole(employee.getEmpRole() != null ? employee.getEmpRole() : emp.getEmpRole());
            emp.setEmpCity(employee.getEmpCity() != null ? employee.getEmpCity() : emp.getEmpCity());
            emp.setSalary(employee.getSalary() != null ? employee.getSalary() : emp.getSalary());
            emp.setPhoneNo(employee.getPhoneNo() != null ? employee.getPhoneNo() : emp.getPhoneNo());
            return emp;
        }
        return null;
    }

    @Override
    public String removeEmployee(String id) {
       Employee isDeleted= map.remove(id);
       if(isDeleted != null){
            return "Employee deleted";
       }
       return "Employee not present";
    }
}
