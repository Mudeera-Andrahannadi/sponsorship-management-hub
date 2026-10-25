package com.sponsorhub.dto;

import java.time.LocalDateTime;

public record CompanyView(
    Integer comp_id,
    String name,
    String contact,
    String job,
    String field,
    String email,
    String phone,
    String status,
    LocalDateTime lock_until,
    Long days_left,
    String held_by,
    boolean mine,
    Integer hold_days,
    Integer lock_days,
    Integer cool_days)
{
}
