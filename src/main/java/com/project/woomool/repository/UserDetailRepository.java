package com.project.woomool.repository;

import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail,Long> {
}
