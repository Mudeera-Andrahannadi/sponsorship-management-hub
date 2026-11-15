package com.sponsorhub.dto;

import com.sponsorhub.entity.Outcome;
import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoryRow(
    Integer dealId,
    Integer compId,
    String company,
    Integer eventId,
    String event,
    String handler,
    Integer handlerId,
    BigDecimal amount,
    Outcome status,
    LocalDate askedOn,
    String notes)
{
}
