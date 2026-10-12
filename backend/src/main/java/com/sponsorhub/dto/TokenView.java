package com.sponsorhub.dto;

import com.sponsorhub.entity.Role;

public record TokenView(String token, Integer user_id, String name, Role role)
{
}
