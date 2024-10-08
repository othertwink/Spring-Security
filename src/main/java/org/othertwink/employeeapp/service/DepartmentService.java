package org.othertwink.employeeapp.service;

import org.othertwink.employeeapp.model.entity.Department;
import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;

public interface DepartmentService {
    Department createDepartment(CompanyDepartment department);
    Department deleteDepartment(Long id);
    Department updateDepartment(Department department);
    Department findById(Long id);
}
