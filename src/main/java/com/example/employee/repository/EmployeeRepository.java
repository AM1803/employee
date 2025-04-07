// EmployeeRepository.java
package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
}
//This interface handles database operations for the employee entity.
//JpaRepository provides pre-built methods for common database tasks (like findAll, save, deleteById).
//Spring Data JPA automatically implements this interface at runtime.