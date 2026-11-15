package com.sponsorhub.service;

import com.sponsorhub.entity.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Rules
{
    private final int holdDays;
    private final int coolDays;
    private final int maxHolds;

    public Rules(@Value("${hub.hold-days}") int holdDays, @Value("${hub.cool-days}") int coolDays,
        @Value("${hub.max-holds}") int maxHolds)
    {
        this.holdDays = holdDays;
        this.coolDays = coolDays;
        this.maxHolds = maxHolds;
    }

    public int maxHolds()
    {
        return maxHolds;
    }

    public int holdFor(Company comp)
    {
        return comp.getLockDays() != null ? comp.getLockDays() : holdDays;
    }

    public int coolFor(Company comp)
    {
        return comp.getCoolDays() != null ? comp.getCoolDays() : coolDays;
    }
}
