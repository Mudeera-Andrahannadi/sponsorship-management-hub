package com.sponsorhub.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Light
{
    Available,
    In_Progress,
    Locked;

    @JsonValue
    public String label()
    {
        return name().replace('_', ' ');
    }
}
