package com.sponsorhub.dto;

import com.sponsorhub.entity.Light;
import java.time.LocalDateTime;

public record Ending(Integer compId, String name, Light status, LocalDateTime lockUntil, String heldBy)
{
}
