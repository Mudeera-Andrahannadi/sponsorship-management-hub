package com.sponsorhub.dto;

import com.sponsorhub.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewUserForm(
    @NotBlank String name,
    @NotBlank String regNo,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password,
    @NotNull Role role)
{
}
