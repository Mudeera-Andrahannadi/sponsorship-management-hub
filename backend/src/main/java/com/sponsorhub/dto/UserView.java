package com.sponsorhub.dto;

import com.sponsorhub.entity.Role;

public record UserView(Integer userId, String name, String regNo, String email, Role role)
{
}
