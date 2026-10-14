package com.sponsorhub.controller;

import com.sponsorhub.dto.CompanyView;
import com.sponsorhub.security.Me;
import com.sponsorhub.service.LockService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
public class CompanyController
{
    private final LockService locks;

    public CompanyController(LockService locks)
    {
        this.locks = locks;
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
}
