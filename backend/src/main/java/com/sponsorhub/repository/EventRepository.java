package com.sponsorhub.repository;

import com.sponsorhub.entity.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Integer>
{
    @Query("select e from Event e join Committee t on t.eventId = e.eventId "
        + "where t.userId = :userId and t.active = true order by e.heldOn")
    List<Event> activeFor(@Param("userId") Integer userId);
}
