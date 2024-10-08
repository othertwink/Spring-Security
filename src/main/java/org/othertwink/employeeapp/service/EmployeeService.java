package org.othertwink.employeeapp.service;

import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;
import org.othertwink.employeeapp.model.entity.enums.EmployeePosition;
import org.othertwink.employeeapp.repository.EmployeeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface EmployeeService {
    Employee createEmployee(String firstName, String lastName, EmployeePosition position, BigDecimal salary, CompanyDepartment department);
    Employee deleteEmployee(Long id);
    Employee updateEmployee(Employee employee);
    Employee findById(Long id);
    EmployeeProjection findProjectedById(Long id);
    Page<Employee> listEmployees(Pageable pageable);
}
