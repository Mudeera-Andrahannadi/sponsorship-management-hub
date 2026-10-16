package com.sponsorhub.dto;

import com.sponsorhub.entity.Outcome;
import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoryRow(
    Integer deal_id,
    Integer comp_id,
    String company,
    Integer event_id,
    String event,
    String handler,
    BigDecimal amount,
    Outcome status,
    LocalDate asked_on,
    String notes)
{
}
