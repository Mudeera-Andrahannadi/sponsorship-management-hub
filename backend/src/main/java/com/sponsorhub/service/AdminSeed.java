package com.sponsorhub.service;

import com.sponsorhub.dto.SignupForm;
import com.sponsorhub.entity.Role;
import com.sponsorhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeed implements ApplicationRunner
{
    private final UserRepository user_repo;
    private final AuthService auth;
    private final String email;
    private final String pass;

    public AdminSeed(UserRepository user_repo, AuthService auth, @Value("${hub.admin-email:}") String email, @Value("${hub.admin-pass:}") String pass)
    {
        this.user_repo = user_repo;
        this.auth = auth;
        this.email = email;
        this.pass = pass;
    }

    @Override
    public void run(ApplicationArguments args)
    {
        if (email.isBlank() || pass.isBlank() || user_repo.count() > 0)
        {
            return;
        }
        if (pass.length() < 8)
        {
            throw new IllegalStateException("ADMIN_PASS needs at least 8 characters");
        }
        auth.create(new SignupForm("Faculty Admin", "ADMIN-001", email, pass), Role.Admin);
    }
}
