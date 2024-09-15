package com.project.woomool.service;

import com.project.woomool.controller.request.UserRecordRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.*;
import com.project.woomool.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRecordService {

    private final UserRecordRepository userRecordRepository;
    private final UserDetailRepository userDetailRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamRecordRepository teamRecordRepository;
    private final TeamDetailRepository  teamDetailRepository;

    @Transactional
    public void addRecord(UserRecordRequest request, CustomOAuth2UserDTO userDto) {
            User user = userRepository.findByEmail(userDto.getEmail());
            UserDetail userDetail = userDetailRepository.findByUser(user);
            UserRecordDto dto =  UserRecordDto.of(request.getAmount());
            UserRecord userRecord = UserRecord.of(dto,user);
            userDetail.updateTotal(request.getAmount());

            if((userDetail.getTodayTotal()>=(userDetail.getRecommendation()*0.8))&&(userDetail.getTodayTotal()<(userDetail.getRecommendation()+350))){
                userDetail.setHasDrankToday(true);
            }else if(userDetail.getTodayTotal()>=(userDetail.getRecommendation()+350)){
                userDetail.setHasDrankToday(false);
                userDetail.setWarnDrankToday(true);
            }

            userRecordRepository.save(userRecord);
            userDetailRepository.save(userDetail);

            if(teamDetailRepository.existsByUser(user)) {
                List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(user);

                List<Team> teams = new ArrayList<>();

                for (TeamDetail teamDetail : teamDetails) {
                    Team team = teamDetail.getTeam();
                    if(!userDetail.isWarnDrankToday()) {
                        teamDetail.addWater(request.getAmount());
                    }
                    if (team != null) {
                        teams.add(team);
                        System.out.println("Added " + team.getName());
                    }
                    teamDetailRepository.save(teamDetail);
                }

                for (Team team : teams) {
                    TeamRecord teamRecord = TeamRecord.of(userRecord, team);
                    if(!userDetail.isWarnDrankToday()){
                        team.updateTotal(request.getAmount());

                    }
                    teamRecordRepository.save(teamRecord);
                    System.out.println(team.getCode() + " 저장됨");
                }

            }
    }

    public List<UserRecordDto> getRecords(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        return userRecordRepository.findAllByUserId(user.getId());
    }




    public List<UserRecordDto> getRecordsById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        List<UserRecordDto> records = userRecordRepository.findAllByUserId(user.getId());
        return records;
    }

    public List<LocalDate> getUserDetailDateList(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        return userRecordRepository.getPassDate(user);
    }

}
