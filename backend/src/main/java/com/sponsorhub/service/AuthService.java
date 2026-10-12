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
    private final UserRepository user_repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository user_repo, PasswordEncoder encoder, JwtService jwt)
    {
        this.user_repo = user_repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public TokenView signup(SignupForm form, Role role)
    {
        User user = new User();
        user.setName(form.name());
        user.setReg_no(form.reg_no());
        user.setEmail(form.email());
        user.setPassword(encoder.encode(form.password()));
        user.setRole(role);
        try
        {
            return token(user_repo.saveAndFlush(user));
        }
        catch (DataIntegrityViolationException ex)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That email or registration number is already in use");
        }
    }

    public TokenView login(LoginForm form)
    {
        User user = user_repo.findByEmail(form.email()).orElse(null);
        if (user == null || !encoder.matches(form.password(), user.getPassword()))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
        }
        return token(user);
    }

    private TokenView token(User user)
    {
        return new TokenView(jwt.issue(user), user.getUser_id(), user.getName(), user.getRole());
    }
}
