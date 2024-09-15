package com.project.woomool.service;

import com.project.woomool.controller.request.CupRequest;
import com.project.woomool.controller.request.UserDetailRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserAttendanceDto;
import com.project.woomool.dto.UserDetailDto;
import com.project.woomool.entity.*;
import com.project.woomool.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    public UserDetailDto addDetail(UserDetailRequest request, CustomOAuth2UserDTO userDto) {
        float bmi = request.getWeight() / (request.getHeight() * request.getHeight());
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = UserDetail.of(request, bmi, user);
        userDetailRepository.save(userDetail);
        return UserDetailDto.of(userDetail);
    }

    public UserDetailDto updateCup(CupRequest request, CustomOAuth2UserDTO userDto) {
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
    public UserDetailDto updateDetail(UserDetailRequest request, CustomOAuth2UserDTO userDto) {
        float bmi = request.getWeight() / (request.getHeight() * request.getHeight());
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);
        userDetail.update(request, bmi, userDetail.getTodayTotal());
        userDetailRepository.save(userDetail);
        return UserDetailDto.of(userDetail);
    }

    public UserDetailDto getUserDetail(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);
        return UserDetailDto.of(userDetail);
    }

    public UserDetailDto getUserDetailByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UserDetail userDetail = userDetailRepository.findByUser(user);
        return UserDetailDto.of(userDetail);
    }

    @Scheduled(cron = "00 33 23 * * *")
    @Synchronized
    @Transactional
    public void autoUpdateWater() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            UserDetail userDetail = userDetailRepository.findByUser(user);
            if (userDetail == null) {
                continue; // 유저 디테일이 없으면 스킵
            }
            processUserAttendance(userDetail, user);
            processTeamUpdates(user, userDetail);
            resetUserDetailForNewDay(userDetail);
        }
    }

    private void processUserAttendance(UserDetail userDetail, User user) {
        UserAttendanceDto dto = UserAttendanceDto.of(userDetail);
        UserAttendance userAttendance = UserAttendance.of(dto, user);
        userAttendanceRepository.save(userAttendance);
    }

    private void processTeamUpdates(User user, UserDetail userDetail) {
        List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(user);
        for (TeamDetail teamDetail : teamDetails) {
            Team team = teamDetail.getTeam();
            float recommendation = userDetail.getRecommendation();
            teamDetail.addPastRecommendation(recommendation);
            team.updateTodayRecommendation(recommendation);
            teamDetailRepository.save(teamDetail);
            teamRepository.save(team);
        }
    }

    private void resetUserDetailForNewDay(UserDetail userDetail) {
        if (userDetail.getWeekDate() >= 6) {
            resetWeeklyData(userDetail);
        } else {
            updateWeeklyData(userDetail);
        }
        userDetail.setAttendance(false);
        userDetail.setTodayTotal(0);
        userDetail.setHasDrankToday(false);
        userDetail.setWarnDrankToday(false);
        userDetailRepository.save(userDetail);
    }

    private void resetWeeklyData(UserDetail userDetail) {
        userDetail.addWeek();
        userDetail.setWeekDate(0);
        userDetail.setWeekBeforeWater(0);
        userDetail.setWeekBeforeRecommendation(0);
        userDetail.setWeekRecommendation(userDetail.getRecommendation() * 7);
    }

    private void updateWeeklyData(UserDetail userDetail) {
        if (userDetail.isWarnDrankToday()) {
            userDetail.addWeekBeforeWater(userDetail.getRecommendation());
        } else {
            userDetail.addWeekBeforeWater(userDetail.getTodayTotal());
        }
        userDetail.addWeekBeforeRecommendation(userDetail.getRecommendation());
        userDetail.addWeekDate();
        userDetail.setWeekRecommendation(userDetail.getWeekBeforeRecommendation()
                + (userDetail.getRecommendation() * (7 - userDetail.getWeekDate())));
    }

    @Scheduled(cron = "00 32 23 * * *")
    @Transactional
    public void autoUpdateRestDay() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            if (team.getDateCount() >= 6) {
                resetTeamData(team);
            } else {
                updateTeamData(team);
            }
            teamRepository.save(team);
        }
    }

    private void resetTeamData(Team team) {
        team.setRecommendation(team.getPastRecommendation() + (team.getTodayRecommendation() * (7 - team.getDateCount())));
        if (team.getGroupTotal() >= (team.getRecommendation() * 0.8)) {
            team.plusCompleteLevel();
        }
        resetTeamDetails(team);
        team.setPastRecommendation(0);
        team.setGroupTotal(0);
        team.setDateCount(0);
        team.setTodayRecommendation(0);
    }

    private void resetTeamDetails(Team team) {
        List<TeamDetail> teamDetails = teamDetailRepository.findAllByTeam(team);
        for (TeamDetail teamDetail : teamDetails) {
            teamDetail.setWaterAmount(0);
            teamDetail.setPastWaterRecommendation(0);
            teamDetailRepository.save(teamDetail);
        }
    }

    private void updateTeamData(Team team) {
        team.setRecommendation(team.getPastRecommendation() + (team.getTodayRecommendation() * (7 - team.getDateCount())));
        team.plusPastRecommendation(team.getTodayRecommendation());
        team.plusDateCount();
        team.setTodayRecommendation(0);
    }
}
