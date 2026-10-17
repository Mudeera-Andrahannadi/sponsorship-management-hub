package com.sponsorhub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record LockForm(@Min(1) @Max(1095) Integer days)
{
}
