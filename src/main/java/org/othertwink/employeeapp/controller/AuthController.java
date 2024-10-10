package org.othertwink.employeeapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.othertwink.employeeapp.model.dto.EmployeeDTO;
import org.othertwink.employeeapp.model.dto.LogInDTO;
import org.othertwink.employeeapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody @Valid EmployeeDTO employee){
        return ResponseEntity.ok(authService.register(employee));
    }
    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody @Valid LogInDTO employee) throws Exception {
        return ResponseEntity.ok(authService.login(employee));
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid LogInDTO employee){
        return ResponseEntity.ok(authService.refreshToken(employee));
    }
}
