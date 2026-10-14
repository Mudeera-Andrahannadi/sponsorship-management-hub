package com.sponsorhub.entity;

public enum Light
{
    Available,
    In_Progress,
    Locked;

    public String label()
    {
        return name().replace('_', ' ');
    }
}
