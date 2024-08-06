package com.project.woomool.service;

import com.project.woomool.controller.request.CupRequest;
import com.project.woomool.controller.request.UserDetailRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserAttendanceDto;
import com.project.woomool.dto.UserDetailDto;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.*;
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
    private final UserAttendanceRepository userAttendanceRepository;
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

    public UserDetailDto attendance(CustomOAuth2UserDTO userDto) {

        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);

        userDetail.setAttendance(true);

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

    @Scheduled(cron = "00 29 03 * * *")
    @Transactional
    public void autoUpdateWater() {

        List <User> users = userRepository.findAll();

        for (User user : users) {
            UserDetail userDetail = userDetailRepository.findByUser(user);
            if (userDetail == null) {
                continue;
            }
            UserAttendanceDto dto = UserAttendanceDto.of(userDetail);
            UserAttendance userAttendance = UserAttendance.of(dto, user);
            userAttendanceRepository.save(userAttendance);

            List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(user);
            for (TeamDetail teamDetail : teamDetails) {
                Team team = teamDetail.getTeam();

                if (userDetail.isWarnDrankToday() && !userDetail.isHasDrankToday()) {
                    teamDetail.addWater(userDetail.getRecommendation());
                    team.updateTotal(userDetail.getRecommendation());
                } else {
                    teamDetail.addWater(userDetail.getTodayTotal());
                    team.updateTotal(userDetail.getTodayTotal());
                }
                teamDetail.addPastRecommendation(userDetail.getRecommendation());
                team.updateTodayRecommendation(userDetail.getRecommendation());

                teamRepository.save(team);
                teamDetailRepository.save(teamDetail);
            }


            if (userDetail.getWeekDate() >= 6) {
                userDetail.addWeek();
                userDetail.setWeekDate(0);
                userDetail.setWeekBeforeWater(0);
                userDetail.setWeekBeforeRecommendation(0);
            } else {
                if (userDetail.isWarnDrankToday()) {
                    userDetail.addWeekBeforeWater(userDetail.getRecommendation());
                } else {
                    userDetail.addWeekBeforeWater(userDetail.getTodayTotal());
                }
                userDetail.addWeekBeforeRecommendation(userDetail.getRecommendation());
                userDetail.addWeekDate();
                userDetail.setWeekRecommendation(userDetail.getWeekBeforeRecommendation() + (userDetail.getRecommendation() * (7 - userDetail.getWeekDate())));

            }
            userDetail.setAttendance(false);

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
