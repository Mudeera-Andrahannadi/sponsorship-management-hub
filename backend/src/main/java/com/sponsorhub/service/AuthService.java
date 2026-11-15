package com.sponsorhub.service;

import com.sponsorhub.dto.LoginForm;
import com.sponsorhub.dto.SignupForm;
import com.sponsorhub.dto.TokenView;
import com.sponsorhub.entity.Role;
import com.sponsorhub.entity.User;
import com.sponsorhub.repository.UserRepository;
import com.sponsorhub.security.JwtService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService
{
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder, JwtService jwt)
    {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public TokenView signup(SignupForm form, Role role)
    {
        return token(create(form, role));
    }

    public User create(SignupForm form, Role role)
    {
        User user = new User();
        user.setName(form.name());
        user.setRegNo(form.regNo());
        user.setEmail(form.email());
        user.setPassword(encoder.encode(form.password()));
        user.setRole(role);
        try
        {
            return userRepo.saveAndFlush(user);
        }
        catch (DataIntegrityViolationException ex)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That email or registration number is already in use");
        }
    }

    public TokenView login(LoginForm form)
    {
        User user = userRepo.findByEmail(form.email()).orElse(null);
        if (user == null || !encoder.matches(form.password(), user.getPassword()))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
        }
        return token(user);
    }

    private TokenView token(User user)
    {
        return new TokenView(jwt.issue(user), user.getUserId(), user.getName(), user.getRole());
    }
}
