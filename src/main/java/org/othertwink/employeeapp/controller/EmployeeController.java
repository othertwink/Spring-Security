package org.othertwink.employeeapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.othertwink.employeeapp.model.dto.EmployeeDTO;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.repository.EmployeeProjection;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EmployeeController {


        private final DepartmentService departmentService;

        private final EmployeeService employeeService;

        private final ObjectMapper objectMapper;


        @GetMapping("/employee/all")
        public ResponseEntity<Page<Employee>> listEmployees(
                @PageableDefault(size = 10, sort = "firstName") Pageable pageable) {
            Page<Employee> employees = employeeService.listEmployees(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(employees);
        }

        @GetMapping("/employee/{employeeId}") // moderator or higher
        @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'MODERATOR')")
        public ResponseEntity<EmployeeProjection> getEmployee(@PathVariable Long employeeId) {
            return ResponseEntity.ok(employeeService.findProjectionById(employeeId));
        }

        @PostMapping(value = "/employee/create")
        @PreAuthorize("hasAnyAuthority('SUPER_ADMIN'")
        public ResponseEntity<Employee> createEmployee(@RequestBody @Valid EmployeeDTO employee) throws IllegalArgumentException {
            Employee createdEmployee = employeeService.createEmployee(employee);
            return ResponseEntity.ok(createdEmployee);
        }

        @PutMapping(value = "/employee/update")
        @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'MODERATOR')")
        public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) throws IllegalArgumentException {
            Employee updatedEmployee = employeeService.updateEmployee(employee);
            return ResponseEntity.ok(updatedEmployee);
        }

        @DeleteMapping(value = "/employee/{employeeId}/delete")
        @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'MODERATOR')")
        public ResponseEntity<Employee> deleteEmployee(@PathVariable Long employeeId) throws IllegalArgumentException, JsonProcessingException {
            Employee deleted = employeeService.deleteEmployee(employeeId);
            return ResponseEntity.ok(deleted);
        }
}
