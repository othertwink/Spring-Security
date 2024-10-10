package org.othertwink.employeeapp.service;

import org.othertwink.employeeapp.model.dto.EmployeeDTO;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.repository.EmployeeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface EmployeeService extends UserDetailsService {
    Employee createEmployee(EmployeeDTO employeeDTO); // TODO dto
    Employee deleteEmployee(Long id);
    Employee updateEmployee(Employee employee);
    Employee findById(Long id);
    EmployeeProjection findProjectionById(Long id);
//    Optional<EmployeeProjection> findProjectedById(Long id);
    Page<Employee> listEmployees(Pageable pageable); // TODO dto

    Employee resetFailedLoginAttempts(Employee employee);

    Employee increaseFailedLoginAttempts(Employee employee);
}
