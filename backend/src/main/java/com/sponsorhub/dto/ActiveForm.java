package com.sponsorhub.dto;

import jakarta.validation.constraints.NotNull;

public record ActiveForm(@NotNull Boolean active)
{
}
