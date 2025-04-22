package com.example.employee.service;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.jasypt.util.text.StrongTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    //Creates a logger
    @Autowired//injects dependency of employeerepository
    private EmployeeRepository employeeRepository;
    private final StrongTextEncryptor textEncryptor;

    public CustomUserDetailsService() {
        textEncryptor = new StrongTextEncryptor();
        textEncryptor.setPassword("technology"); // Same password as in application.properties
    }

    @Override//overrides the loaduserbyusername method
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user: {}", username);//logs the username being loaded
        Optional<Employee> employeeOptional = employeeRepository.findByUsername(username);//Finds employee by username

        if (employeeOptional.isEmpty()) {
            logger.warn("User not found: {}", username);
            throw new UsernameNotFoundException("User not found");
        }

        Employee emp = employeeOptional.get();
        String decryptedPassword = textEncryptor.decrypt(emp.getPassword());
        logger.info("User found: {}", emp.getUsername());
        logger.info("User roles: {}", emp.getRoles());
        logger.info("Password from database (encrypted): {}", emp.getPassword()); // Log encrypted password
        logger.info("Password after decryption: {}", decryptedPassword); // Log decrypted password

        return User.withUsername(emp.getUsername())
                .password(decryptedPassword) // Use the decrypted password for authentication
                .roles(emp.getRoles().split(","))//set the roles
                .build();
    }
}