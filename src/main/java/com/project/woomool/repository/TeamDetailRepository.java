package com.project.woomool.repository;

import com.project.woomool.entity.Team;
import com.project.woomool.entity.TeamDetail;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamDetailRepository extends JpaRepository<TeamDetail,Long> {
    TeamDetail findTeamDetailByUser(User user);
    Boolean existsByUser(User user);
}
