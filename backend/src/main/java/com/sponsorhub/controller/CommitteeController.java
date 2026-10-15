package com.sponsorhub.controller;

import com.sponsorhub.dto.ActiveForm;
import com.sponsorhub.dto.AssignForm;
import com.sponsorhub.dto.EventView;
import com.sponsorhub.dto.Made;
import com.sponsorhub.dto.TeamView;
import com.sponsorhub.security.Me;
import com.sponsorhub.service.EventService;
import com.sponsorhub.service.TeamService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/committees")
public class CommitteeController
{
    private final TeamService teams;
    private final EventService events;

    public CommitteeController(TeamService teams, EventService events)
    {
        this.teams = teams;
        this.events = events;
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('Admin')")
    public Made assign(@Valid @RequestBody AssignForm form)
    {
        return new Made(teams.assign(form));
    }

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public List<TeamView> everyone()
    {
        return teams.everyone();
    }

    @GetMapping("/mine")
    public List<EventView> mine(@AuthenticationPrincipal Me me)
    {
        return events.mine(me);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public void setActive(@PathVariable Integer id, @Valid @RequestBody ActiveForm form)
    {
        teams.setActive(id, form.active());
    }
}
