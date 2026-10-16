package com.sponsorhub.controller;

import com.sponsorhub.dto.HistoryRow;
import com.sponsorhub.service.SponsorshipService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history")
public class HistoryController
{
    private final SponsorshipService deals;

    public HistoryController(SponsorshipService deals)
    {
        this.deals = deals;
    }

    @GetMapping
    public List<HistoryRow> all()
    {
        return deals.history(null, null);
    }

    @GetMapping("/company/{id}")
    public List<HistoryRow> forCompany(@PathVariable Integer id)
    {
        return deals.history(id, null);
    }

    @GetMapping("/event/{id}")
    public List<HistoryRow> forEvent(@PathVariable Integer id)
    {
        return deals.history(null, id);
    }
}
