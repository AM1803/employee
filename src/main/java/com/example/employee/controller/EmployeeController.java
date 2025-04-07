// emp_controller.java
package com.example.employee.controller;

import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController//rest controller to handle web requests
@RequestMapping("/employees")//sets the base url for all methods in this controller to /employees
public class EmployeeController {

    @Autowired//injects an instance of employee service
    private EmployeeService employeeService;//to use employee service

    @GetMapping//handles all the get requests to /employees(eg-to get all employees)
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();//calls the service to get all employees
    }

    @GetMapping("/{id}")//handles get requests by /{id}(eg- get employees by id)
    public Employee getEmployeeById(@PathVariable Long id) {//extracts id from the url
        return employeeService.getEmployeeById(id);//calls service to get employee by id
    }

    @PostMapping//handles post request to employees(eg- to create a new employee detail)
    public Employee createEmployee(@RequestBody Employee employee) {//extracts data from request body
        return employeeService.createEmployee(employee);//calls the service to create an employee
    }

    @PutMapping("/{id}")//handles put request by /employees/{id}(eg-to update employee)
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {//extracts id and emp data
        return employeeService.updateEmployee(id, employee);//calls service to update emp data
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {//extracts id from employee
        employeeService.deleteEmployee(id);//calls service to delete employee
    }
}

