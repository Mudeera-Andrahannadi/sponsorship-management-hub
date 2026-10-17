package com.sponsorhub.controller;

import com.sponsorhub.dto.CompanyForm;
import com.sponsorhub.dto.CompanyView;
import com.sponsorhub.security.Me;
import com.sponsorhub.service.DirectoryService;
import com.sponsorhub.service.LockService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
public class CompanyController
{
    private final LockService locks;
    private final DirectoryService directory;

    public CompanyController(LockService locks, DirectoryService directory)
    {
        this.locks = locks;
        this.directory = directory;
    }

    @GetMapping
    public List<CompanyView> list(@AuthenticationPrincipal Me me, @RequestParam(required = false) String field,
        @RequestParam(required = false) String status)
    {
        return locks.list(me, field, status);
    }

    @PostMapping("/{id}/lock")
    public CompanyView lock(@AuthenticationPrincipal Me me, @PathVariable Integer id)
    {
        return locks.claim(id, me);
    }

    @PostMapping("/{id}/release")
    public CompanyView release(@AuthenticationPrincipal Me me, @PathVariable Integer id)
    {
        return locks.release(id, me);
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyView create(@AuthenticationPrincipal Me me, @Valid @RequestBody CompanyForm form)
    {
        return directory.create(form, me);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public CompanyView update(@AuthenticationPrincipal Me me, @PathVariable Integer id, @Valid @RequestBody CompanyForm form)
    {
        return directory.update(id, form, me);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id)
    {
        directory.delete(id);
    }
}
