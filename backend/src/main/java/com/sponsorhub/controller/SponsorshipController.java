package com.sponsorhub.controller;

import com.sponsorhub.dto.Made;
import com.sponsorhub.dto.SponsorForm;
import com.sponsorhub.dto.UpdateForm;
import com.sponsorhub.security.Me;
import com.sponsorhub.service.SponsorshipService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sponsorships")
public class SponsorshipController
{
    private final SponsorshipService deals;

    public SponsorshipController(SponsorshipService deals)
    {
        this.deals = deals;
    }

    @PostMapping
    @PreAuthorize("hasRole('Student')")
    @ResponseStatus(HttpStatus.CREATED)
    public Made log(@AuthenticationPrincipal Me me, @Valid @RequestBody SponsorForm form)
    {
        return new Made(deals.log(form, me));
    }

    @PatchMapping("/{id}/status")
    public void update(@AuthenticationPrincipal Me me, @PathVariable Integer id, @Valid @RequestBody UpdateForm form)
    {
        deals.update(id, form, me);
    }
}
