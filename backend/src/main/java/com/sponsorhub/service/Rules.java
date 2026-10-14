package com.sponsorhub.service;

import com.sponsorhub.entity.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Rules
{
    private final int hold_days;
    private final int cool_days;

    public Rules(@Value("${hub.hold-days}") int hold_days, @Value("${hub.cool-days}") int cool_days)
    {
        this.hold_days = hold_days;
        this.cool_days = cool_days;
    }

    public int holdFor(Company comp)
    {
        return comp.getLock_days() != null ? comp.getLock_days() : hold_days;
    }

    public int coolFor(Company comp)
    {
        return comp.getCool_days() != null ? comp.getCool_days() : cool_days;
    }
}
