package com.sponsorhub.repository;

import com.sponsorhub.dto.HistoryRow;
import com.sponsorhub.dto.Total;
import com.sponsorhub.entity.Sponsorship;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer>
{
    @Query("select new com.sponsorhub.dto.HistoryRow(s.dealId, s.compId, c.name, s.eventId, e.name, u.name, s.handler, "
        + "s.amount, s.status, s.askedOn, s.notes) "
        + "from Sponsorship s join Company c on c.compId = s.compId join Event e on e.eventId = s.eventId "
        + "join User u on u.userId = s.handler "
        + "where (:compId is null or s.compId = :compId) and (:eventId is null or s.eventId = :eventId) "
        + "order by s.askedOn desc, s.dealId desc")
    List<HistoryRow> rows(@Param("compId") Integer compId, @Param("eventId") Integer eventId);

    @Query("select new com.sponsorhub.dto.Total(e.name, sum(s.amount)) "
        + "from Sponsorship s join Event e on e.eventId = s.eventId "
        + "where s.status = com.sponsorhub.entity.Outcome.Approved "
        + "group by e.eventId, e.name order by sum(s.amount) desc")
    List<Total> byEvent();

    @Query("select new com.sponsorhub.dto.Total(c.name, sum(s.amount)) "
        + "from Sponsorship s join Company c on c.compId = s.compId "
        + "where s.status = com.sponsorhub.entity.Outcome.Approved "
        + "group by c.compId, c.name order by sum(s.amount) desc")
    List<Total> bySponsor(Pageable page);
}
