package com.sponsorhub.controller;

import com.sponsorhub.dto.EventForm;
import com.sponsorhub.dto.EventView;
import com.sponsorhub.service.EventService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController
{
    private final EventService events;

    public EventController(EventService events)
    {
        this.events = events;
    }

    @GetMapping
    public List<EventView> list()
    {
        return events.list();
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public EventView create(@Valid @RequestBody EventForm form)
    {
        return events.create(form);
    }
}
