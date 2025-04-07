package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public String registerEmployee(Employee employee) {
        employee.setRoles("USER");
        // Password will be stored as is (no encoding for now)
        employeeRepository.save(employee);
        return "Employee registered successfully!";
    }

    // The loginUser method is no longer directly responsible for checking credentials.
    // Authentication is now handled by the AuthenticationManager in the AuthenticationController.
    // You can remove or comment out the loginUser method if you don't have other uses for it.
    // public String loginUser(String username, String password) { ... }
}