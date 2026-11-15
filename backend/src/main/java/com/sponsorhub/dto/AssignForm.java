package com.sponsorhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssignForm(@NotNull Integer userId, @NotNull Integer eventId, @NotBlank String post)
{
}
