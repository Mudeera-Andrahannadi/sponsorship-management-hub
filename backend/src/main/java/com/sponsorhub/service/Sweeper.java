package com.sponsorhub.service;

import com.sponsorhub.repository.CompanyRepository;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableScheduling
public class Sweeper
{
    private final CompanyRepository compRepo;

    public Sweeper(CompanyRepository compRepo)
    {
        this.compRepo = compRepo;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void sweep()
    {
        compRepo.freeExpired(LocalDateTime.now());
    }
}
