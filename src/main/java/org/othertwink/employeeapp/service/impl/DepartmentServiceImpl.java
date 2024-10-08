package org.othertwink.employeeapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.othertwink.employeeapp.model.entity.Department;
import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;
import org.othertwink.employeeapp.repository.DepartmentRepo;
import org.othertwink.employeeapp.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;
    @Override
    @Transactional
    public Department createDepartment(CompanyDepartment department) {
        Department createdDepartment = Department.builder().department(department).build();
        return departmentRepo.save(createdDepartment);
    }

    @Override
    @Transactional
    public Department deleteDepartment(Long id) {
        Department department = findById(id);
        departmentRepo.delete(department);
        return department;
    }

    @Override
    @Transactional
    public Department updateDepartment(Department department) {
        Department existingDepartment = findById(department.getId());
        existingDepartment.setDepartment(department.getDepartment());
        return existingDepartment;
    }

    @Override
    @Transactional
    public Department findById(Long id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No department under Id " + id + " found"));
    }
}
