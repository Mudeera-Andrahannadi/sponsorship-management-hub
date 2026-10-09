package com.sponsorhub.repository;

import com.sponsorhub.entity.Committee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitteeRepository extends JpaRepository<Committee, Integer>
{
}
