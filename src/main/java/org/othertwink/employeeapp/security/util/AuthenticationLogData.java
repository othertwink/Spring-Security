package org.othertwink.employeeapp.security.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationLogData {
    private String username;
    private String status;
    private String reason;
}