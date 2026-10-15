package com.sponsorhub.dto;

import java.time.LocalDate;

public record EventView(Integer event_id, String name, String society, String held_month, LocalDate held_on)
{
}
