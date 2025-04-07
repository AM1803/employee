package com.example.employee.service;

import com.example.employee.entity.Employee;//allows us to use employee class
import com.example.employee.repository.EmployeeRepository;//allow to use repo
import org.springframework.beans.factory.annotation.Autowired;//allows to inject dependencies
import org.springframework.stereotype.Service;//marks this as spring service
import java.util.List;//Allows us to use lists
import java.util.Optional;//Allows to handle cases where employee must not exist

@Service //Tells spring this is service class to be managed by spring
public class EmployeeService {

    @Autowired//injects an instance of emp repo
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {//Method to get all employees
        return employeeRepository.findAll();//finds all employee in the db and return them
    }

    public Employee createEmployee(Employee employee) {//Method to create an employee
        return employeeRepository.save(employee);//save employee in database
    }

    public Employee updateEmployee(Long id, Employee employee) {//to update existing details
        employee.setId(id);//id to identify which has to be updated
        return employeeRepository.save(employee);//to save in repo
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Employee getEmployeeById(Long id) {//to find employee by id
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);//Tries to find employee
        return optionalEmployee.orElse(null);//if found return employee if not then null
    }
}