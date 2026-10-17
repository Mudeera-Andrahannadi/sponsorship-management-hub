package com.sponsorhub.dto;

import com.sponsorhub.entity.Role;

public record UserView(Integer user_id, String name, String reg_no, String email, Role role)
{
}
