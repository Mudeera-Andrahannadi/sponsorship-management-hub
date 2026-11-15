package com.sponsorhub.dto;

import com.sponsorhub.entity.Role;

public record TokenView(String token, Integer userId, String name, Role role)
{
}
