package com.sponsorhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CompanyForm(@NotBlank String name, String contact, String job, String field, @Email String email, String phone)
{
}
