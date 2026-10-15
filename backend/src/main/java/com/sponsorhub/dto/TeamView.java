package com.sponsorhub.dto;

public record TeamView(Integer team_id, Integer user_id, String name, Integer event_id, String event, String post, Boolean active)
{
}
