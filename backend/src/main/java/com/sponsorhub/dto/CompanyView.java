package com.sponsorhub.dto;

import java.time.LocalDateTime;

public record CompanyView(
    Integer compId,
    String name,
    String contact,
    String job,
    String field,
    String email,
    String phone,
    String status,
    LocalDateTime lockUntil,
    Long daysLeft,
    String heldBy,
    boolean mine,
    Integer holdDays,
    Integer lockDays,
    Integer coolDays)
{
}
