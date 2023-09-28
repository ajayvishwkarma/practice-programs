package com.demo2.entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class Employee {
    private String empId;
    private String empName;
    private String empCity;
    private String phoneNo;
    private String empRole;
    private String salary;
}
