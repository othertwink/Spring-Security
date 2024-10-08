package org.othertwink.employeeapp.repository;

import org.othertwink.employeeapp.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    EmployeeProjection findProjectedById(Long id);
}
