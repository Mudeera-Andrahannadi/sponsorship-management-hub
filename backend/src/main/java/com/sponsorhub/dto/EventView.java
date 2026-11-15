package com.sponsorhub.dto;

import java.time.LocalDate;

public record EventView(Integer eventId, String name, String society, String heldMonth, LocalDate heldOn)
{
}
