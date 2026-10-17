package com.sponsorhub.dto;

import com.sponsorhub.entity.Light;
import java.time.LocalDateTime;

public record Ending(Integer comp_id, String name, Light status, LocalDateTime lock_until, String held_by)
{
}
