package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private final StrongTextEncryptor textEncryptor;

    public UserService() {
        textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword("your-encryption-password-here"); // Same password as in application.properties
    }

    public String registerEmployee(Employee employee) {
        employee.setRoles("USER");
        String encryptedPassword = textEncryptor.encrypt(employee.getPassword());
        employee.setEncryptedPassword(encryptedPassword);
        // We are now saving the encrypted password
        employee.setPassword(null); // It's good practice to nullify the plain text password
        employeeRepository.save(employee);
        return "Employee registered successfully!";
    }

    // The loginUser method is no longer directly responsible for checking credentials.
    // Authentication is now handled by the AuthenticationManager in the AuthenticationController.
    // You can remove or comment out the loginUser method if you don't have other uses for it.
    // public String loginUser(String username, String password) { ... }
}