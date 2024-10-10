package org.othertwink.employeeapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LogInDTO(
        @NotBlank
        String username,
        @Size(min = 6, max = 50)
        String password)
{}

