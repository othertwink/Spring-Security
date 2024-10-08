package org.othertwink.employeeapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.othertwink.employeeapp.model.entity.Department;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.security.util.JWTUtils;
import org.othertwink.employeeapp.service.DepartmentService;
import org.othertwink.employeeapp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class RestController {


    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;
    

    @GetMapping("/employee/all")
    public ResponseEntity<Page<Employee>> listEmployees(
            @PageableDefault(size = 10, sort = "firstName") Pageable pageable) {
        Page<Employee> employees = employeeService.listEmployees(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }


    @GetMapping("/home")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<Employee>> homePage(
            @PageableDefault(size = 10, sort = "firstName") Pageable pageable) {
        Page<Employee> employees = employeeService.listEmployees(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/employee/{employeeId}") // moderator or higher
    @PreAuthorize("hasRole('MODERATOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> getEmployee(@PathVariable Long employeeId) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(employeeService.findProjectedById(employeeId)));
    }

    @PostMapping(value = "/employee/create", consumes = MediaType.APPLICATION_JSON_VALUE) // admin
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) throws IllegalArgumentException, JsonProcessingException {
        Employee createdEmployee = employeeService.createEmployee(employee.getFirstName(), employee.getLastName(), employee.getPosition(), employee.getSalary(), employee.getDepartment());
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(createdEmployee));
    }

    @PutMapping(value = "/employee/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee) throws IllegalArgumentException, JsonProcessingException {
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(updatedEmployee));
    }

    @DeleteMapping(value = "/employee/{employeeId}/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId) throws IllegalArgumentException, JsonProcessingException {
        Employee deleted = employeeService.deleteEmployee(employeeId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(deleted));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<String> getDepartment(@PathVariable Long departmentId) throws JsonProcessingException, IllegalArgumentException  {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(departmentService.findById(departmentId)));
    }

    @PostMapping(value = "/department/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createDepartment(@RequestBody Department department) throws IllegalArgumentException, JsonProcessingException {
        Department createdDepartment = departmentService.createDepartment(department.getDepartment());
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(createdDepartment));
    }

    @PutMapping(value = "/department/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDepartment(@RequestBody Department department) throws IllegalArgumentException, JsonProcessingException {
        Department updatedDepartment = departmentService.updateDepartment(department);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(updatedDepartment));
    }

    @DeleteMapping(value = "/department/{departmentId}/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteDepartment(@PathVariable Long departmentId) throws IllegalArgumentException, JsonProcessingException {
        Department deleted = departmentService.deleteDepartment(departmentId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(deleted));
    }

}
