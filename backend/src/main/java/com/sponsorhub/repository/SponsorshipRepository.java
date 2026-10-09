package com.sponsorhub.repository;

import com.sponsorhub.entity.Sponsorship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer>
{
}
