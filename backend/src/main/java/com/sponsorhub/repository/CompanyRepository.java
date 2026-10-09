package com.sponsorhub.repository;

import com.sponsorhub.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer>
{
}
