package org.othertwink.employeeapp.model.dto.response;

import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;

public record EmployeeResponseDto(
        String firstname,
        String lastname,
        String username,
        String position,
        Double salary,
        CompanyDepartment department) {
}
