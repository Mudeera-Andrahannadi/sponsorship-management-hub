package com.sponsorhub.repository;

import com.sponsorhub.dto.TeamView;
import com.sponsorhub.entity.Committee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommitteeRepository extends JpaRepository<Committee, Integer>
{
    @Query("select count(t) > 0 from Committee t where t.userId = :userId and t.active = true")
    boolean hasActive(@Param("userId") Integer userId);

    @Query("select count(t) > 0 from Committee t where t.userId = :userId and t.eventId = :eventId and t.active = true")
    boolean isActiveIn(@Param("userId") Integer userId, @Param("eventId") Integer eventId);

    @Query("select new com.sponsorhub.dto.TeamView(t.teamId, t.userId, u.name, t.eventId, e.name, t.post, t.active) "
        + "from Committee t join User u on u.userId = t.userId join Event e on e.eventId = t.eventId "
        + "order by e.heldOn, u.name")
    List<TeamView> everyone();
}
