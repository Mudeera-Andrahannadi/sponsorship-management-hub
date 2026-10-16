package com.sponsorhub.repository;

import com.sponsorhub.dto.HistoryRow;
import com.sponsorhub.entity.Sponsorship;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer>
{
    @Query("select new com.sponsorhub.dto.HistoryRow(s.deal_id, s.comp_id, c.name, s.event_id, e.name, u.name, "
        + "s.amount, s.status, s.asked_on, s.notes) "
        + "from Sponsorship s join Company c on c.comp_id = s.comp_id join Event e on e.event_id = s.event_id "
        + "join User u on u.user_id = s.handler "
        + "where (:comp_id is null or s.comp_id = :comp_id) and (:event_id is null or s.event_id = :event_id) "
        + "order by s.asked_on desc, s.deal_id desc")
    List<HistoryRow> rows(@Param("comp_id") Integer comp_id, @Param("event_id") Integer event_id);
}
