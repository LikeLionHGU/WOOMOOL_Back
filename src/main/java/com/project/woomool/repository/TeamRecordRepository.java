package com.project.woomool.repository;

import com.project.woomool.entity.Team;
import com.project.woomool.entity.TeamDetail;
import com.project.woomool.entity.TeamRecord;
import com.project.woomool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRecordRepository extends JpaRepository<TeamRecord,Long> {

    List<TeamRecord> findAllByUserAndTeam(User user, Team team);
    @Query("SELECT tr.date, SUM(tr.amount) FROM TeamRecord tr WHERE tr.team = :team GROUP BY tr.date")
    List<TeamRecord> findAllByTeamGroupedByDay(@Param("team") Team team);
}

