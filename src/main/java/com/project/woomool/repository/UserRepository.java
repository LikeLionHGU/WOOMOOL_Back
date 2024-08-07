package com.project.woomool.repository;

import com.project.woomool.entity.TeamDetail;
import com.project.woomool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    Boolean existsByNickName(String nickName);


}
