package com.project.woomool.service;

import com.project.woomool.controller.request.UserDetailRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserDetailDto;
import com.project.woomool.entity.Team;
import com.project.woomool.entity.TeamDetail;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.repository.TeamDetailRepository;
import com.project.woomool.repository.TeamRepository;
import com.project.woomool.repository.UserDetailRepository;
import com.project.woomool.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final TeamDetailRepository teamDetailRepository;
    private final TeamRepository teamRepository;

    public UserDetailDto addDetail(UserDetailRequest request , CustomOAuth2UserDTO userDto) {

        float bmi = (request.getWeight() / (request.getHeight() * request.getHeight()));
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = UserDetail.of(request,bmi,user);

        userDetailRepository.save(userDetail);
        return UserDetailDto.of(userDetail);
    }

    @Scheduled(cron = "58 59 23 * * *")
    @Transactional
    public void autoUpdateWater() {
        List<UserDetail> userDetails = userDetailRepository.findAll();
        for (UserDetail userDetail : userDetails) {
            TeamDetail teamDetail = teamDetailRepository.findTeamDetailByUser(userDetail.getUser());
            Team team = teamDetail.getTeam();
            team.updateTotal(userDetail.getTodayTotal());
            userDetail.setTodayTotal(0);
            userDetailRepository.save(userDetail);
            teamRepository.save(team);
        }
    }



}
