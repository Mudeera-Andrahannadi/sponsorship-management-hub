package com.sponsorhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupForm(
    @NotBlank String name,
    @NotBlank String regNo,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password)
{
}
