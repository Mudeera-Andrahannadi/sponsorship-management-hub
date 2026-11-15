package com.sponsorhub.dto;

public record TeamView(Integer teamId, Integer userId, String name, Integer eventId, String event, String post, Boolean active)
{
}
