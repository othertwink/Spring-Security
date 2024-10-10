package org.othertwink.employeeapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.othertwink.employeeapp.model.entity.Department;
import org.othertwink.employeeapp.model.entity.enums.Role;

import java.math.BigDecimal;

public record EmployeeDTO(@Size(min = 1, max = 50)
                         String firstname,
                          @Size(min = 1, max = 50)
                         String lastname,
                          @Size(min = 6, max = 50)
                         String password,
                          @NotBlank
                         String username,
                          @NotBlank
                         String position,
                          @NotNull
                         Role role,
                          @NotNull
                          BigDecimal salary,
                          @NotNull Department department)
{}
