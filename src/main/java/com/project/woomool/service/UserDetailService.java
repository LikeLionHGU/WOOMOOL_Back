package com.project.woomool.service;

import com.project.woomool.controller.request.CupRequest;
import com.project.woomool.controller.request.UserDetailRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserDetailDto;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.Team;
import com.project.woomool.entity.TeamDetail;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final TeamDetailRepository teamDetailRepository;
    private final TeamRepository teamRepository;
    private final UserRecordRepository userRecordRepository;

    public UserDetailDto addDetail(UserDetailRequest request , CustomOAuth2UserDTO userDto) {

        float bmi = (request.getWeight() / (request.getHeight() * request.getHeight()));
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = UserDetail.of(request,bmi,user);

        userDetailRepository.save(userDetail);
        return UserDetailDto.of(userDetail);
    }

    public UserDetailDto updateCup(CupRequest request , CustomOAuth2UserDTO userDto) {

        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);

        userDetail.changeCup(request.getCup());

        userDetailRepository.save(userDetail);
        return UserDetailDto.of(userDetail);
    }
    @Transactional
    public UserDetailDto updateDetail(UserDetailRequest request , CustomOAuth2UserDTO userDto) {

        float bmi = (request.getWeight() / (request.getHeight() * request.getHeight()));
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);

        userDetail.update(request,bmi, userDetail.getTodayTotal());

        userDetailRepository.save(userDetail);
        return UserDetailDto.of(userDetail);
    }

    public UserDetailDto getUserDetail(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);
        return UserDetailDto.of(userDetail);
    }

    public UserDetailDto getUserDetailByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UserDetail userDetail = userDetailRepository.findByUser(user);
        return UserDetailDto.of(userDetail);
    }

    @Scheduled(cron = "58 59 23 * * *")
    @Transactional
    public void autoUpdateWater() {
        List<UserDetail> userDetails = userDetailRepository.findAll();
        for (UserDetail userDetail : userDetails) {
            List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(userDetail.getUser());
            for(TeamDetail teamDetail:teamDetails) {
                Team team = teamDetail.getTeam();

                if (userDetail.isWarnDrankToday() && !userDetail.isHasDrankToday()) {
                    teamDetail.addWater(userDetail.getRecommendation());
                    team.updateTotal(userDetail.getRecommendation());
                } else {
                    teamDetail.addWater(userDetail.getTodayTotal());
                    team.updateTotal(userDetail.getTodayTotal());
                }

                team.updateTodayRecommendation(userDetail.getRecommendation());

                teamRepository.save(team);
                teamDetailRepository.save(teamDetail);
            }

            userDetail.setTodayTotal(0);
            if (userDetail.isHasDrankToday()) {
                    userDetail.addDrankLevel();
            }
            userDetail.setHasDrankToday(false);
            userDetail.setWarnDrankToday(false);
            userDetailRepository.save(userDetail);

        }
    }






}
