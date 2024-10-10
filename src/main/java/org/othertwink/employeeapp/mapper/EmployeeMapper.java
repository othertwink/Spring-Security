package org.othertwink.employeeapp.mapper;

import lombok.RequiredArgsConstructor;
import org.othertwink.employeeapp.model.dto.EmployeeDTO;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.model.entity.enums.EmployeePosition;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper implements Mapper<EmployeeDTO, Employee> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Employee map(EmployeeDTO employee) {
        return Employee.builder()
                .firstName(employee.firstname())
                .lastName(employee.lastname())
                .username(employee.username())
                .password(employee.password())
                .password(passwordEncoder.encode(employee.password()))
                .role(employee.role())
                .salary(employee.salary())
                .position(EmployeePosition.valueOf(employee.position()))
                .isAccountNonLocked(true)
                .department(employee.department().getDepartment())
                .build();
    }

    public Employee map(EmployeeDTO employeeDto, Employee employee) {
        employee.setFirstName(employeeDto.firstname());
        employee.setLastName(employeeDto.lastname());
        employee.setUsername(employeeDto.username());
        employee.setPassword(employeeDto.password());
        employee.setRole(employeeDto.role());
        employee.setSalary(employeeDto.salary());
        employee.setPosition(EmployeePosition.valueOf(employeeDto.position()));
        employee.setDepartment(employee.getDepartment());
        return employee;
    }
}
