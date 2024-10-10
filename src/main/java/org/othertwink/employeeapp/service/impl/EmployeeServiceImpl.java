package org.othertwink.employeeapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.othertwink.employeeapp.mapper.EmployeeMapper;
import org.othertwink.employeeapp.model.dto.EmployeeDTO;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.repository.EmployeeProjection;
import org.othertwink.employeeapp.repository.EmployeeRepo;
import org.othertwink.employeeapp.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepo employeeRepo;

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, EmployeeMapper employeeMapper) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
    }


    @Override
    @Transactional
    public EmployeeProjection findProjectionById(Long id) {
        return employeeRepo.findProjectedById(id)
                .orElseThrow(() -> new EntityNotFoundException("No employee under Id " + id + " found"));
    }

    @Override
    @Transactional
    public Page<Employee> listEmployees(Pageable pageable) {
        return employeeRepo.findAll(pageable);
    }

    // TODO принимать DTO из контроллера
    @Override
    @Transactional
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        return Optional.of(employeeDTO)
                .map(employeeMapper::map)
                .map(employee -> {
                    employee.setDepartment(employeeDTO.department().getDepartment());
                    return employee;
                })
                .map(employeeRepo::save)
                .orElseThrow();
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

    @Override
    public Employee findById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public  Employee resetFailedLoginAttempts(Employee employee) {
        employee.setFailedLoginAttempts(0);
        employee.setAccountNonLocked(true);
        return employeeRepo.save(employee);
    }

    @Override
    public Employee increaseFailedLoginAttempts(Employee employee) {
        employee.setFailedLoginAttempts(employee.getFailedLoginAttempts()+1);
        return employeeRepo.save(employee);
    }
}
