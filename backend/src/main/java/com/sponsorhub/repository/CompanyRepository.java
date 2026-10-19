package com.sponsorhub.repository;

import com.sponsorhub.dto.Ending;
import com.sponsorhub.dto.LightCount;
import com.sponsorhub.entity.Company;
import com.sponsorhub.entity.Light;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, Integer>
{
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Company c set c.status = :busy, c.lock_until = :until, c.lock_by = :user_id "
        + "where c.comp_id = :comp_id and (c.status = :free or c.lock_until < :now)")
    int claimRow(@Param("comp_id") Integer comp_id, @Param("user_id") Integer user_id, @Param("until") LocalDateTime until,
        @Param("now") LocalDateTime now, @Param("busy") Light busy, @Param("free") Light free);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Company c set c.status = :free, c.lock_until = null, c.lock_by = null "
        + "where c.status <> :free and c.lock_until < :now")
    int freeRows(@Param("now") LocalDateTime now, @Param("free") Light free);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Company c set c.status = :free, c.lock_until = null, c.lock_by = null "
        + "where c.comp_id = :comp_id and c.status = :busy and c.lock_by = :user_id")
    int releaseRow(@Param("comp_id") Integer comp_id, @Param("user_id") Integer user_id,
        @Param("busy") Light busy, @Param("free") Light free);

    @Query("select new com.sponsorhub.dto.LightCount(c.status, count(c)) from Company c group by c.status")
    List<LightCount> counts();

    @Query("select new com.sponsorhub.dto.Ending(c.comp_id, c.name, c.status, c.lock_until, u.name) "
        + "from Company c left join User u on u.user_id = c.lock_by "
        + "where c.status <> :free and c.lock_until between :now and :soon order by c.lock_until")
    List<Ending> endingRows(@Param("now") LocalDateTime now, @Param("soon") LocalDateTime soon, @Param("free") Light free);

    @Query("select count(c) from Company c where c.lock_by = :user_id and c.status = :busy and c.lock_until > :now")
    long holding(@Param("user_id") Integer user_id, @Param("now") LocalDateTime now, @Param("busy") Light busy);

    default long held(Integer user_id, LocalDateTime now)
    {
        return holding(user_id, now, Light.In_Progress);
    }

    default List<Ending> ending(LocalDateTime now, LocalDateTime soon)
    {
        return endingRows(now, soon, Light.Available);
    }

    default int claim(Integer comp_id, Integer user_id, LocalDateTime until, LocalDateTime now)
    {
        return claimRow(comp_id, user_id, until, now, Light.In_Progress, Light.Available);
    }

    default int freeExpired(LocalDateTime now)
    {
        return freeRows(now, Light.Available);
    }

    default int release(Integer comp_id, Integer user_id)
    {
        return releaseRow(comp_id, user_id, Light.In_Progress, Light.Available);
    }
}
