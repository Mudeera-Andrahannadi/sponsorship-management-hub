package com.sponsorhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssignForm(@NotNull Integer user_id, @NotNull Integer event_id, @NotBlank String post)
{
}
