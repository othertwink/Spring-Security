package org.othertwink.employeeapp.repository;

import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;
import org.othertwink.employeeapp.model.entity.enums.EmployeePosition;
import org.springframework.beans.factory.annotation.Value;

public interface EmployeeProjection {
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();

    EmployeePosition getPosition();

    CompanyDepartment getDepartment();
}
