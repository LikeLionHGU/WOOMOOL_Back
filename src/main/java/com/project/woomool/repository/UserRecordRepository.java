package com.project.woomool.repository;

import com.project.woomool.entity.User;
import com.project.woomool.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRecordRepository extends JpaRepository<UserRecord,Long> {

    @Query("SELECT SUM(ur.amount) FROM UserRecord ur WHERE ur.user.id = :userId")
    Float sumAmountByUserId(@Param("userId") Long userId);

}
