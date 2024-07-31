package com.project.woomool.repository;

import com.project.woomool.entity.Team;
import com.project.woomool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {

    boolean existsTeamByCode(String code);
    Team findTeamByCode(String code);

    Team findTeamById(Long Id);

    boolean existsTeamByName(String name);

    @Query("SELECT SUM(tr.amount) FROM TeamRecord tr WHERE tr.user = :user and tr.team =:team")
    Float sumAmountByUserAndTeam(User user, Team team);
}
