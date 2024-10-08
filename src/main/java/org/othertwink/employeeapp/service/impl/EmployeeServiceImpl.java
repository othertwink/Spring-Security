package org.othertwink.employeeapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.model.entity.enums.CompanyDepartment;
import org.othertwink.employeeapp.model.entity.enums.EmployeePosition;
import org.othertwink.employeeapp.repository.EmployeeProjection;
import org.othertwink.employeeapp.repository.EmployeeRepo;
import org.othertwink.employeeapp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    @Transactional
    public Employee findById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No employee under Id " + id + " found"));
    }

    @Override
    @Transactional
    public EmployeeProjection findProjectedById(Long id) {
        return employeeRepo.findProjectedById(id);
    }

    @Override
    @Transactional
    public Page<Employee> listEmployees(Pageable pageable) {
        return employeeRepo.findAll(pageable);
    }

    @Override
    @Transactional
    public Employee createEmployee(String firstName, String lastName, EmployeePosition position, BigDecimal salary, CompanyDepartment department) {
        Employee createdEmployee = Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .position(position)
                .salary(salary)
                .department(department)
                .build();
        return employeeRepo.save(createdEmployee);
    }

    @Override
    @Transactional
    public Employee deleteEmployee(Long id) {
        Employee employee = findById(id);
        employeeRepo.delete(employee);
        return employee;
    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee) {
        Employee existingEmployee = findById(employee.getId());
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setPosition(employee.getPosition());
        existingEmployee.setSalary(employee.getSalary());
        existingEmployee.setDepartment(employee.getDepartment());
        return employeeRepo.save(existingEmployee);
    }
}
