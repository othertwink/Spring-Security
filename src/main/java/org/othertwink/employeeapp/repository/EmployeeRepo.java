package org.othertwink.employeeapp.repository;

import org.othertwink.employeeapp.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<EmployeeProjection> findProjectedById(Long id);
    Optional<Employee> findByUsername(String username);
}
