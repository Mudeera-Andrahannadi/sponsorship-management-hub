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
    private final CompanyRepository comp_repo;
    private final LockService locks;

    public DirectoryService(CompanyRepository comp_repo, LockService locks)
    {
        this.comp_repo = comp_repo;
        this.locks = locks;
    }

    public CompanyView create(CompanyForm form, Me me)
    {
        Company comp = new Company();
        comp.setStatus(Light.Available);
        fill(comp, form);
        return locks.show(comp_repo.save(comp).getComp_id(), me);
    }

    @Transactional
    public CompanyView update(Integer comp_id, CompanyForm form, Me me)
    {
        Company comp = locks.find(comp_id);
        fill(comp, form);
        return locks.show(comp_id, me);
    }

    public void delete(Integer comp_id)
    {
        locks.find(comp_id);
        try
        {
            comp_repo.deleteById(comp_id);
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
