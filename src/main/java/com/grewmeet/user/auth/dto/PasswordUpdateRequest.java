package com.grewmeet.user.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
    @NotBlank(message = "Current password cannot be blank")
    String currentPassword,

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, message = "New password must be equal or greater than 8 characters")
    String newPassword
) {}
