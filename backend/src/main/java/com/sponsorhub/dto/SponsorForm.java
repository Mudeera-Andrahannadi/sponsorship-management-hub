package com.sponsorhub.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SponsorForm(
    @NotNull Integer comp_id,
    @NotNull Integer event_id,
    @NotNull @DecimalMin("0.01") @DecimalMax("99999999.99") BigDecimal amount,
    String notes)
{
}
