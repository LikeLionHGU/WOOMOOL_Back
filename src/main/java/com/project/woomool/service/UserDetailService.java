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

    @Scheduled(cron = "00 58 23 * * *")
    @Transactional
    public void autoUpdateWater() {

        List <User> users = userRepository.findAll();

        for (User user : users) {
            UserDetail userDetail = userDetailRepository.findByUser(user); //모든 유저 찾기
            if (userDetail == null) { //유저가 가입만하고 자기꺼 입력안했으면 스킵
                continue;
            }
            UserAttendanceDto dto = UserAttendanceDto.of(userDetail); //출석체크
            UserAttendance userAttendance = UserAttendance.of(dto, user); //출섹체크 기록
            userAttendanceRepository.save(userAttendance); //저장

            List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(user); //팀 찾기

            for (TeamDetail teamDetail : teamDetails) {
                Team team = teamDetail.getTeam();

                teamDetail.addPastRecommendation(userDetail.getRecommendation());
                team.updateTodayRecommendation(userDetail.getRecommendation()); //그룹별 하루 추천량 만드는 듯 근데 리셋 하고 해야하지 않나?

                teamRepository.save(team);
                teamDetailRepository.save(teamDetail);
            }


            if (userDetail.getWeekDate() >= 6) {
                userDetail.addWeek();
                userDetail.setWeekDate(0);
                userDetail.setWeekBeforeWater(0);
                userDetail.setWeekBeforeRecommendation(0);
                userDetail.setWeekRecommendation(userDetail.getRecommendation() * 7);
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
        //모든 팀 업데이트 해야함
        //모든 개인 초기화가 위에서 이루어짐
    }

    @Scheduled(cron = "00 55 23 * * *")
    @Transactional
    public void autoUpdateRestDay() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            if(team.getDateCount()>=6){

                team.setRecommendation(team.getPastRecommendation()+(team.getTodayRecommendation()*(7-team.getDateCount())));

                if(team.getGroupTotal()>=(team.getRecommendation()*0.8)){
                    team.plusCompleteLevel();
                }
                List<TeamDetail> teamDetails = teamDetailRepository.findAllByTeam(team);
                if (!teamDetails.isEmpty()) {
                    for (TeamDetail teamDetail : teamDetails) {
                        teamDetail.setWaterAmount(0);
                        teamDetail.setPastWaterRecommendation(0);
                        teamDetailRepository.save(teamDetail);
                    }
                }

                team.setPastRecommendation(0);
                team.setGroupTotal(0);
                team.setDateCount(0);
                team.setTodayRecommendation(0);

            }else {
                team.setRecommendation(team.getPastRecommendation()+(team.getTodayRecommendation()*(7-team.getDateCount())));
                team.plusPastRecommendation(team.getTodayRecommendation());
                team.plusDateCount();
                team.setTodayRecommendation(0);

            }
            teamRepository.save(team);
        }
    }




}
