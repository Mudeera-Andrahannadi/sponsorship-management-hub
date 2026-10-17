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
    private final UserRepository user_repo;
    private final AuthService auth;

    public AdminService(LockService locks, Rules rules, UserRepository user_repo, AuthService auth)
    {
        this.locks = locks;
        this.rules = rules;
        this.user_repo = user_repo;
        this.auth = auth;
    }

    @Transactional
    public CompanyView setDays(Integer comp_id, DaysForm form, Me me)
    {
        Company comp = locks.find(comp_id);
        comp.setLock_days(form.lock_days());
        comp.setCool_days(form.cool_days());
        return locks.show(comp_id, me);
    }

    @Transactional
    public CompanyView lock(Integer comp_id, Integer days, Me me)
    {
        Company comp = locks.find(comp_id);
        int span = days != null ? days : rules.coolFor(comp);
        comp.setStatus(Light.Locked);
        comp.setLock_until(LocalDateTime.now().plusDays(span));
        comp.setLock_by(me.user_id());
        return locks.show(comp_id, me);
    }

    @Transactional
    public CompanyView unlock(Integer comp_id, Me me)
    {
        Company comp = locks.find(comp_id);
        comp.setStatus(Light.Available);
        comp.setLock_until(null);
        comp.setLock_by(null);
        return locks.show(comp_id, me);
    }

    public List<UserView> users(Role role)
    {
        return user_repo.findAll().stream()
            .filter(item -> role == null || item.getRole() == role)
            .sorted(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER))
            .map(this::view)
            .toList();
    }

    public UserView addUser(NewUserForm form)
    {
        SignupForm person = new SignupForm(form.name(), form.reg_no(), form.email(), form.password());
        return view(auth.create(person, form.role()));
    }

    private UserView view(User user)
    {
        return new UserView(user.getUser_id(), user.getName(), user.getReg_no(), user.getEmail(), user.getRole());
    }
}
