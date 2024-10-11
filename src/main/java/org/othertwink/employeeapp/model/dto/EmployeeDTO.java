package org.othertwink.employeeapp.model.dto;

import org.othertwink.employeeapp.model.entity.Department;
import org.othertwink.employeeapp.model.entity.enums.Role;

import java.math.BigDecimal;

public record EmployeeDTO(
        String firstname,
        String lastname,
        String password,
        String username,
        String email,
        String position,
        Role role,
        BigDecimal salary,
        Department department
) {}
