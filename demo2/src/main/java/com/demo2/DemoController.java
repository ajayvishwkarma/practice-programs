package com.demo2;

import com.demo2.entity.Employee;
import com.demo2.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {
    @Autowired
    private EmpService empService;

    @GetMapping("/hello")
    public String helloTeam(){
        return "Hello Team...";
    }

    @PostMapping("/add")
    public String addEmployee(@RequestBody Employee employee){

        return empService.addEmployee(employee);
    }


    @GetMapping("/getEmployee/{id}")
    public Employee getEmployeeById(@PathVariable String id){
        return empService.getEmployee(id);
    }

    @PutMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable String id,@RequestBody Employee employee){
        Employee employee1= empService.updateEmployee(id,employee);
        if(employee1==null){
            return "Employee Not Found";
        }
        return "Employee Updated";
    }

    @DeleteMapping("/deleteEmployee/{id}")
    public String deleteEmp(@PathVariable String id){
        return empService.removeEmployee(id);
    }

}
