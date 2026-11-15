package com.sponsorhub.service;

import com.sponsorhub.dto.CompanyView;
import com.sponsorhub.dto.DaysForm;
import com.sponsorhub.dto.NewUserForm;
import com.sponsorhub.dto.SignupForm;
import com.sponsorhub.dto.UserView;
import com.sponsorhub.entity.Company;
import com.sponsorhub.entity.Light;
import com.sponsorhub.entity.Role;
import com.sponsorhub.entity.User;
import com.sponsorhub.repository.UserRepository;
import com.sponsorhub.security.Me;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService
{
    private final LockService locks;
    private final Rules rules;
    private final UserRepository userRepo;
    private final AuthService auth;

    public AdminService(LockService locks, Rules rules, UserRepository userRepo, AuthService auth)
    {
        this.locks = locks;
        this.rules = rules;
        this.userRepo = userRepo;
        this.auth = auth;
    }

    @Transactional
    public CompanyView setDays(Integer compId, DaysForm form, Me me)
    {
        Company comp = locks.find(compId);
        comp.setLockDays(form.lockDays());
        comp.setCoolDays(form.coolDays());
        return locks.show(compId, me);
    }

    @Transactional
    public CompanyView lock(Integer compId, Integer days, Me me)
    {
        Company comp = locks.find(compId);
        int span = days != null ? days : rules.coolFor(comp);
        comp.setStatus(Light.Locked);
        comp.setLockUntil(LocalDateTime.now().plusDays(span));
        comp.setLockBy(me.userId());
        return locks.show(compId, me);
    }

    @Transactional
    public CompanyView unlock(Integer compId, Me me)
    {
        Company comp = locks.find(compId);
        comp.setStatus(Light.Available);
        comp.setLockUntil(null);
        comp.setLockBy(null);
        return locks.show(compId, me);
    }

    public List<UserView> users(Role role)
    {
        return userRepo.findAll().stream()
            .filter(item -> role == null || item.getRole() == role)
            .sorted(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER))
            .map(this::view)
            .toList();
    }

    public UserView addUser(NewUserForm form)
    {
        SignupForm person = new SignupForm(form.name(), form.regNo(), form.email(), form.password());
        return view(auth.create(person, form.role()));
    }

    private UserView view(User user)
    {
        return new UserView(user.getUserId(), user.getName(), user.getRegNo(), user.getEmail(), user.getRole());
    }
}
