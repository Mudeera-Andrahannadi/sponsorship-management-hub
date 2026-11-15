package com.sponsorhub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record DaysForm(@Min(1) @Max(365) Integer lockDays, @Min(1) @Max(1095) Integer coolDays)
{
}
