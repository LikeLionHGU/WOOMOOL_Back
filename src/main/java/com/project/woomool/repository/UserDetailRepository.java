package com.project.woomool.repository;

import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail,Long> {
    UserDetail findByUser(User user);


}
