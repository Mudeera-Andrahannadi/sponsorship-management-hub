package com.sponsorhub.controller;

import com.sponsorhub.dto.LoginForm;
import com.sponsorhub.dto.SignupForm;
import com.sponsorhub.dto.TokenView;
import com.sponsorhub.entity.Role;
import com.sponsorhub.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final AuthService auth;

    public AuthController(AuthService auth)
    {
        this.auth = auth;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenView register(@Valid @RequestBody SignupForm form)
    {
        return auth.signup(form, Role.Student);
    }

    @PostMapping("/login")
    public TokenView login(@Valid @RequestBody LoginForm form)
    {
        return auth.login(form);
    }
}
