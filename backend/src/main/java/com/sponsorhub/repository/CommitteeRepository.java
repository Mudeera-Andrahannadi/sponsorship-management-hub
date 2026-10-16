package com.sponsorhub.repository;

import com.sponsorhub.dto.TeamView;
import com.sponsorhub.entity.Committee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommitteeRepository extends JpaRepository<Committee, Integer>
{
    @Query("select count(t) > 0 from Committee t where t.user_id = :user_id and t.active = true")
    boolean hasActive(@Param("user_id") Integer user_id);

    @Query("select count(t) > 0 from Committee t where t.user_id = :user_id and t.event_id = :event_id and t.active = true")
    boolean isActiveIn(@Param("user_id") Integer user_id, @Param("event_id") Integer event_id);

    @Query("select new com.sponsorhub.dto.TeamView(t.team_id, t.user_id, u.name, t.event_id, e.name, t.post, t.active) "
        + "from Committee t join User u on u.user_id = t.user_id join Event e on e.event_id = t.event_id "
        + "order by e.held_on, u.name")
    List<TeamView> everyone();
}
