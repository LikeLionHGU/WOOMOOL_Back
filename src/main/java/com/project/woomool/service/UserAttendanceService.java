package com.project.woomool.service;

import com.project.woomool.controller.request.UserRecordRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserAttendanceDto;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.*;
import com.project.woomool.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAttendanceService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;
    private final UserAttendanceRepository userAttendanceRepository;


    public List<UserAttendanceDto> getAttendance(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        return userAttendanceRepository.findAllByUserId(user.getId());
    }

    @Scheduled(cron = "57 59 23 * * *")
    @Transactional
    public void autoUpdateAttendance() {
        List <User> users = userRepository.findAll();
        for (User user : users) {
            UserDetail userDetail = userDetailRepository.findByUser(user);
            UserAttendanceDto dto =  UserAttendanceDto.of(userDetail);
            UserAttendance userAttendance = UserAttendance.of(dto, user);

            if(userDetail.getWeekDate()>=6){
                userDetail.addWeek();
                userDetail.setWeekDate(0);
                userDetail.setWeekBeforeWater(0);
                userDetail.setWeekBeforeRecommendation(0);
            }else{
                if(userDetail.isWarnDrankToday()){
                    userDetail.addWeekBeforeWater(userDetail.getRecommendation());
                }else {
                    userDetail.addWeekBeforeWater(userDetail.getTodayTotal());
                }
                userDetail.addWeekBeforeRecommendation(userDetail.getRecommendation());
                userDetail.addWeekDate();
                userDetail.setWeekRecommendation(userDetail.getWeekBeforeRecommendation()+(userDetail.getRecommendation()*(7-userDetail.getWeekDate())));

            }
            userDetail.setAttendance(false);
            userAttendanceRepository.save(userAttendance);
            userDetailRepository.save(userDetail);
        }

        }


}
