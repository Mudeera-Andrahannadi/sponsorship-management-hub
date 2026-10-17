package com.sponsorhub.service;

import com.sponsorhub.dto.CompanyView;
import com.sponsorhub.dto.DaysForm;
import com.sponsorhub.entity.Company;
import com.sponsorhub.entity.Light;
import com.sponsorhub.security.Me;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService
{
    private final LockService locks;
    private final Rules rules;

    public AdminService(LockService locks, Rules rules)
    {
        this.locks = locks;
        this.rules = rules;
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
}
