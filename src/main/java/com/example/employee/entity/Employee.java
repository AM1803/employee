package com.example.employee.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;//allows us to automatically generate id
import jakarta.persistence.GenerationType;//specifies id generation strategy
import jakarta.persistence.Id;
import org.hibernate.annotations.Formula;

@Entity//tells spring that this class represent database table
public class Employee {
    @Id//marks id as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)//automatically generates id
    private Long id;
    private String name;
    private String department;
    private String username;
   // private String password;
   // private String roles;
   // private String encryptedPassword; // Store the encrypted password

    private String password;  // Virtual field for decrypted password (use with caution)
    private String roles;
    // Constructors, getters, setters
    public Employee(){}//default constructor used by spring & hibernate

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDepartment() {return department;}
    public void setDepartment(String department) {this.department = department;}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    //public String getEncryptedPassword() { return encryptedPassword; }
  //  public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

}