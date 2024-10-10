package org.othertwink.employeeapp.controller;

import lombok.RequiredArgsConstructor;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EmployeeService employeeService;

    @PostMapping("/unlock/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> unlockUser(@PathVariable String username) {
        Employee employee = (Employee) employeeService.loadUserByUsername(username);
        employeeService.resetFailedLoginAttempts(employee);
        return ResponseEntity.ok("Employee " + username + " has been unlocked.");
    }
}
