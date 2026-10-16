package com.sponsorhub.dto;

import com.sponsorhub.entity.Outcome;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record UpdateForm(
    Outcome status,
    @DecimalMin("0.01") @DecimalMax("99999999.99") BigDecimal amount,
    String notes)
{
}
