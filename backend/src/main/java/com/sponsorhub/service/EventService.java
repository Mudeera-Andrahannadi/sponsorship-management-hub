package com.sponsorhub.service;

import com.sponsorhub.dto.EventForm;
import com.sponsorhub.dto.EventView;
import com.sponsorhub.entity.Event;
import com.sponsorhub.repository.EventRepository;
import com.sponsorhub.security.Me;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class EventService
{
    private final EventRepository event_repo;

    public EventService(EventRepository event_repo)
    {
        this.event_repo = event_repo;
    }

    public EventView create(EventForm form)
    {
        Event event = new Event();
        event.setName(form.name());
        event.setSociety(form.society());
        event.setHeld_on(form.held_on());
        event.setHeld_month(form.held_on().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        return view(event_repo.save(event));
    }

    public List<EventView> list()
    {
        Comparator<Event> by_date = Comparator.comparing(Event::getHeld_on, Comparator.nullsLast(Comparator.naturalOrder()));
        return event_repo.findAll().stream().sorted(by_date).map(this::view).toList();
    }

    public List<EventView> mine(Me me)
    {
        return event_repo.activeFor(me.user_id()).stream().map(this::view).toList();
    }

    private EventView view(Event event)
    {
        return new EventView(event.getEvent_id(), event.getName(), event.getSociety(), event.getHeld_month(), event.getHeld_on());
    }
}
