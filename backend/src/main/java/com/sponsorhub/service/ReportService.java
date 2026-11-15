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
    private final CompanyRepository compRepo;
    private final SponsorshipRepository dealRepo;

    public ReportService(CompanyRepository compRepo, SponsorshipRepository dealRepo)
    {
        this.compRepo = compRepo;
        this.dealRepo = dealRepo;
    }

    @Transactional
    public Analytics analytics()
    {
        LocalDateTime now = LocalDateTime.now();
        compRepo.freeExpired(now);
        Map<String, Long> lights = new LinkedHashMap<>();
        for (Light light : Light.values())
        {
            lights.put(light.label(), 0L);
        }
        compRepo.counts().forEach(row -> lights.put(row.light().label(), row.n()));
        List<Total> events = dealRepo.byEvent();
        BigDecimal raised = events.stream().map(Total::sum).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Analytics(lights, raised, events, dealRepo.bySponsor(PageRequest.of(0, 5)), compRepo.ending(now, now.plusHours(48)));
    }
}
