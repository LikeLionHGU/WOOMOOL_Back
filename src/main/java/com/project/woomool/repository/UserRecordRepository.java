package com.project.woomool.repository;

import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository

public interface UserRecordRepository extends JpaRepository<UserRecord,Long> {

    @Query("SELECT SUM(ur.amount) FROM UserRecord ur WHERE ur.user.id = :userId")
    Float sumAmountByUserId(@Param("userId") Long userId);

    List<UserRecordDto> findAllByUserId(Long userId);

    @Query("SELECT SUM(ur.amount) FROM UserRecord ur")
    Long sumAmount();

    @Query("SELECT DISTINCT ur.date FROM UserRecord ur " +
            "JOIN UserDetail ud ON ur.user.id = ud.user.id " +
            "GROUP BY ur.date ")
    List<LocalDate> getPassDate(@Param("user") User user);

//    @Query("SELECT DISTINCT ur.date FROM UserRecord ur " +
//            "JOIN UserDetail ud ON ur.user.id = ud.user.id " +
//            "GROUP BY ur.date " +
//            "HAVING ur.amount >= (ud.recommendation) * 1000")

}
