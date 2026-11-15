package com.sponsorhub.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record Analytics(Map<String, Long> lights, BigDecimal raised, List<Total> byEvent, List<Total> topSponsors, List<Ending> ending)
{
}
