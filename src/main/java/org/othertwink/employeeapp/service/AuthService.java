package org.othertwink.employeeapp.service;

import org.othertwink.employeeapp.model.dto.EmployeeDTO;
import org.othertwink.employeeapp.model.dto.LogInDTO;

public interface AuthService {
    String register(EmployeeDTO employee);

    String login(LogInDTO employee) throws Exception;

    String refreshToken(LogInDTO employee);
}
