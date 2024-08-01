package com.project.woomool.repository;

import com.project.woomool.dto.UserAttendanceDto;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserAttendance;
import com.project.woomool.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAttendanceRepository extends JpaRepository<UserAttendance,Long> {

    List<UserAttendanceDto> findAllByUserId(Long userId);
}
