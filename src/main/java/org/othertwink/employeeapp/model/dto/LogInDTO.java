package org.othertwink.employeeapp.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LogInDTO(
        String username,
        String password)
{}

