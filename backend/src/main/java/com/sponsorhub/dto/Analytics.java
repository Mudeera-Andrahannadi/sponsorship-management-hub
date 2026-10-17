package com.sponsorhub.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record Analytics(Map<String, Long> lights, BigDecimal raised, List<Total> by_event, List<Total> top_sponsors, List<Ending> ending)
{
}
