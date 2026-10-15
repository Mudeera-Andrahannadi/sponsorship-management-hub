package com.sponsorhub.repository;

import com.sponsorhub.entity.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Integer>
{
    @Query("select e from Event e join Committee t on t.event_id = e.event_id "
        + "where t.user_id = :user_id and t.active = true order by e.held_on")
    List<Event> activeFor(@Param("user_id") Integer user_id);
}
