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
    private final EventRepository eventRepo;

    public EventService(EventRepository eventRepo)
    {
        this.eventRepo = eventRepo;
    }

    public EventView create(EventForm form)
    {
        Event event = new Event();
        event.setName(form.name());
        event.setSociety(form.society());
        event.setHeldOn(form.heldOn());
        event.setHeldMonth(form.heldOn().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        return view(eventRepo.save(event));
    }

    public List<EventView> list()
    {
        Comparator<Event> byDate = Comparator.comparing(Event::getHeldOn, Comparator.nullsLast(Comparator.naturalOrder()));
        return eventRepo.findAll().stream().sorted(byDate).map(this::view).toList();
    }

    public List<EventView> mine(Me me)
    {
        return eventRepo.activeFor(me.userId()).stream().map(this::view).toList();
    }

    private EventView view(Event event)
    {
        return new EventView(event.getEventId(), event.getName(), event.getSociety(), event.getHeldMonth(), event.getHeldOn());
    }
}
