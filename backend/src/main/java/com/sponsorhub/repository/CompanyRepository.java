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
    @Query("update Company c set c.status = :busy, c.lockUntil = :until, c.lockBy = :userId "
        + "where c.compId = :compId and (c.status = :free or c.lockUntil < :now)")
    int claimRow(@Param("compId") Integer compId, @Param("userId") Integer userId, @Param("until") LocalDateTime until,
        @Param("now") LocalDateTime now, @Param("busy") Light busy, @Param("free") Light free);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Company c set c.status = :free, c.lockUntil = null, c.lockBy = null "
        + "where c.status <> :free and c.lockUntil < :now")
    int freeRows(@Param("now") LocalDateTime now, @Param("free") Light free);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Company c set c.status = :free, c.lockUntil = null, c.lockBy = null "
        + "where c.compId = :compId and c.status = :busy and c.lockBy = :userId")
    int releaseRow(@Param("compId") Integer compId, @Param("userId") Integer userId,
        @Param("busy") Light busy, @Param("free") Light free);

    @Query("select new com.sponsorhub.dto.LightCount(c.status, count(c)) from Company c group by c.status")
    List<LightCount> counts();

    @Query("select new com.sponsorhub.dto.Ending(c.compId, c.name, c.status, c.lockUntil, u.name) "
        + "from Company c left join User u on u.userId = c.lockBy "
        + "where c.status <> :free and c.lockUntil between :now and :soon order by c.lockUntil")
    List<Ending> endingRows(@Param("now") LocalDateTime now, @Param("soon") LocalDateTime soon, @Param("free") Light free);

    @Query("select count(c) from Company c where c.lockBy = :userId and c.status = :busy and c.lockUntil > :now")
    long holding(@Param("userId") Integer userId, @Param("now") LocalDateTime now, @Param("busy") Light busy);

    default long held(Integer userId, LocalDateTime now)
    {
        return holding(userId, now, Light.In_Progress);
    }

    default List<Ending> ending(LocalDateTime now, LocalDateTime soon)
    {
        return endingRows(now, soon, Light.Available);
    }

    default int claim(Integer compId, Integer userId, LocalDateTime until, LocalDateTime now)
    {
        return claimRow(compId, userId, until, now, Light.In_Progress, Light.Available);
    }

    default int freeExpired(LocalDateTime now)
    {
        return freeRows(now, Light.Available);
    }

    default int release(Integer compId, Integer userId)
    {
        return releaseRow(compId, userId, Light.In_Progress, Light.Available);
    }
}
