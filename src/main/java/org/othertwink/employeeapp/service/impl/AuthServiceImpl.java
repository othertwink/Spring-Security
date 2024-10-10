package org.othertwink.employeeapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.othertwink.employeeapp.exception.auth.BlockedException;
import org.othertwink.employeeapp.model.dto.EmployeeDTO;
import org.othertwink.employeeapp.model.dto.LogInDTO;
import org.othertwink.employeeapp.model.entity.Employee;
import org.othertwink.employeeapp.repository.EmployeeRepo;
import org.othertwink.employeeapp.security.util.JWTUtils;
import org.othertwink.employeeapp.service.AuthService;
import org.othertwink.employeeapp.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final EmployeeService employeeService;
    private final EmployeeRepo employeeRepo;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    @Value("${app.security.max-failed-attempts}")
    private int MAX_FAILED_ATTEMPTS;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    public String register(EmployeeDTO employee) {
        employeeService.createEmployee(employee);
        UserDetails userDetails = employeeService.loadUserByUsername(employee.username());
        return jwtUtils.generateToken(userDetails);
    }

    public String login(LogInDTO employee) throws Exception {

        UserDetails userDetails = employeeService.loadUserByUsername(employee.username());

        if (!userDetails.isAccountNonLocked()) {
            throw new Exception("User is blocked");
        }

        Employee existingEmployee = employeeRepo.findByUsername(userDetails.getUsername())
                .orElseThrow();
        if (!passwordEncoder.matches(employee.password(), existingEmployee.getPassword())) {
            existingEmployee.setFailedLoginAttempts(existingEmployee.getFailedLoginAttempts() + 1);
            employeeRepo.flush();

            // Проверка на блокировку аккаунта
            if (existingEmployee.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                existingEmployee.setAccountNonLocked(false);
                logger.warn(String.format("%s account is locked", existingEmployee.getUsername()));
                throw new BlockedException("Too many failed attempts to log in");
            }

            throw new Exception("Invalid username or password");
        }

        // Если пароль верный, сбрасываем количество неудачных попыток
        existingEmployee.setFailedLoginAttempts(0);
        employeeRepo.flush();

        // Аутентификация
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                employee.password()); // Передаем открытый пароль для аутентификации

        authenticationManager.authenticate(authentication);

        return jwtUtils.generateRefreshToken(userDetails);
    }

    @Override
    public String refreshToken(LogInDTO employee) {
        UserDetails userDetails = employeeService.loadUserByUsername(employee.username());
        return jwtUtils.generateToken(userDetails);
    }
}