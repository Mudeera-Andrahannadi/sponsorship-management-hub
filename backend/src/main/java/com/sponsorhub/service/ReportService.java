package com.sponsorhub.service;

import com.sponsorhub.dto.Analytics;
import com.sponsorhub.dto.Total;
import com.sponsorhub.entity.Light;
import com.sponsorhub.repository.CompanyRepository;
import com.sponsorhub.repository.SponsorshipRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService
{
    private final CompanyRepository comp_repo;
    private final SponsorshipRepository deal_repo;

    public ReportService(CompanyRepository comp_repo, SponsorshipRepository deal_repo)
    {
        this.comp_repo = comp_repo;
        this.deal_repo = deal_repo;
    }

    @Transactional
    public Analytics analytics()
    {
        LocalDateTime now = LocalDateTime.now();
        comp_repo.freeExpired(now);
        Map<String, Long> lights = new LinkedHashMap<>();
        for (Light light : Light.values())
        {
            lights.put(light.label(), 0L);
        }
        comp_repo.counts().forEach(row -> lights.put(row.light().label(), row.n()));
        List<Total> events = deal_repo.byEvent();
        BigDecimal raised = events.stream().map(Total::sum).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Analytics(lights, raised, events, deal_repo.bySponsor(PageRequest.of(0, 5)), comp_repo.ending(now, now.plusHours(48)));
    }
}
