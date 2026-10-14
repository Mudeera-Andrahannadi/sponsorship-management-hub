package com.sponsorhub.repository;

import com.sponsorhub.entity.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommitteeRepository extends JpaRepository<Committee, Integer>
{
    @Query("select count(t) > 0 from Committee t where t.user_id = :user_id and t.active = true")
    boolean hasActive(@Param("user_id") Integer user_id);
}
