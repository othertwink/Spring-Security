package org.othertwink.employeeapp.controller;

import lombok.RequiredArgsConstructor;
import org.othertwink.employeeapp.model.dto.EmployeeDTO;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.service.EmployeeService;
import org.othertwink.employeeapp.service.impl.GitHubOAuth2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final GitHubOAuth2Service gitHubOAuth2Service;

    private final EmployeeService employeeService;

    @GetMapping("/user/{username}/revoke-access")
    @PreAuthorize("hasRole('ADMIN')")
    public String revokeAccess(@PathVariable String username) {
        gitHubOAuth2Service.revokeAccess(username);
        return "redirect:/admin";
    }

    @PostMapping("/employee/register")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> signUp(@RequestBody EmployeeDTO employee){
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.findById(employeeId));
    }
}
