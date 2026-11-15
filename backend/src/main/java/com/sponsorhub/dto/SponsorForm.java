package com.sponsorhub.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SponsorForm(
    @NotNull Integer compId,
    @NotNull Integer eventId,
    @NotNull @DecimalMin("0.01") @DecimalMax("99999999.99") BigDecimal amount,
    String notes)
{
}
