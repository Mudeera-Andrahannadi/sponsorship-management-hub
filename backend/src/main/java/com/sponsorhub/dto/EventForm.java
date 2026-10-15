package com.sponsorhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EventForm(@NotBlank String name, @NotBlank String society, @NotNull LocalDate held_on)
{
}
