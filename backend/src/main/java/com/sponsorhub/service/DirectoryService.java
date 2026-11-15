package com.sponsorhub.service;

import com.sponsorhub.dto.CompanyForm;
import com.sponsorhub.dto.CompanyView;
import com.sponsorhub.entity.Company;
import com.sponsorhub.entity.Light;
import com.sponsorhub.repository.CompanyRepository;
import com.sponsorhub.security.Me;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DirectoryService
{
    private final CompanyRepository compRepo;
    private final LockService locks;

    public DirectoryService(CompanyRepository compRepo, LockService locks)
    {
        this.compRepo = compRepo;
        this.locks = locks;
    }

    public CompanyView create(CompanyForm form, Me me)
    {
        Company comp = new Company();
        comp.setStatus(Light.Available);
        fill(comp, form);
        return locks.show(compRepo.save(comp).getCompId(), me);
    }

    @Transactional
    public CompanyView update(Integer compId, CompanyForm form, Me me)
    {
        Company comp = locks.find(compId);
        fill(comp, form);
        return locks.show(compId, me);
    }

    public void delete(Integer compId)
    {
        locks.find(compId);
        try
        {
            compRepo.deleteById(compId);
        }
        catch (DataIntegrityViolationException ex)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This company has sponsorship history, so it cannot be deleted");
        }
    }

    private void fill(Company comp, CompanyForm form)
    {
        comp.setName(form.name());
        comp.setContact(form.contact());
        comp.setJob(form.job());
        comp.setField(form.field());
        comp.setEmail(form.email());
        comp.setPhone(form.phone());
    }
}
