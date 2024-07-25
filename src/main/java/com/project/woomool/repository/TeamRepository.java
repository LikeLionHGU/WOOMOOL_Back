package com.project.woomool.repository;

import com.project.woomool.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {

    boolean existsTeamByCode(String code);
    Team findTeamByCode(String code);

    Team findTeamById(Long Id);
}
