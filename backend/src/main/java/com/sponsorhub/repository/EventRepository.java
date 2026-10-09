package com.sponsorhub.repository;

import com.sponsorhub.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer>
{
}
