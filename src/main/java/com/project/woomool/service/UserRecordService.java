package com.project.woomool.service;

import com.project.woomool.controller.request.UserRecordRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.*;
import com.project.woomool.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final TeamDetailRepository teamDetailRepository;

    @Transactional
    public void addRecord(UserRecordRequest request, CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);

        // 사용자 물 기록 생성
        UserRecordDto dto = UserRecordDto.of(request.getAmount());
        UserRecord userRecord = UserRecord.of(dto, user);

        // 오늘 마신 총량 업데이트
        userDetail.updateTotal(request.getAmount());

        // 사용자가 추천량의 80% 이상 마셨을 때 상태 업데이트
        if ((userDetail.getTodayTotal() >= (userDetail.getRecommendation() * 0.8))
                && (userDetail.getTodayTotal() < (userDetail.getRecommendation() + 350))) {
            userDetail.setHasDrankToday(true);
        } else if (userDetail.getTodayTotal() >= (userDetail.getRecommendation() + 350)) {
            userDetail.setHasDrankToday(false);
            userDetail.setWarnDrankToday(true);
        }

        // 사용자 기록 저장
        userRecordRepository.save(userRecord);
        userDetailRepository.save(userDetail);

        // 팀 관련 처리
        if (teamDetailRepository.existsByUser(user)) {
            List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(user);
            List<Team> teams = new ArrayList<>();

            // 팀 디테일 업데이트
            for (TeamDetail teamDetail : teamDetails) {
                Team team = teamDetail.getTeam();

                // 경고 상태가 아니면 팀 디테일에 물 기록 추가
                if (!userDetail.isWarnDrankToday()) {
                    teamDetail.addWater(request.getAmount());
                }

                if (team != null) {
                    teams.add(team);
                    System.out.println("Added " + team.getName());
                }

                // 팀 디테일 저장
                teamDetailRepository.save(teamDetail);
            }

            // 팀 기록 업데이트
            for (Team team : teams) {
                TeamRecord teamRecord = TeamRecord.of(userRecord, team);

                // 팀의 총량 업데이트는 이미 teamDetail에서 처리되므로 중복 업데이트 방지
                teamRecordRepository.save(teamRecord);
                System.out.println(team.getCode() + " 저장됨");
            }
        }
    }

    // 사용자 기록 조회
    public List<UserRecordDto> getRecords(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        return userRecordRepository.findAllByUserId(user.getId());
    }

    // 특정 사용자 ID로 기록 조회
    public List<UserRecordDto> getRecordsById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return userRecordRepository.findAllByUserId(user.getId());
    }

    // 사용자 물 기록 날짜 조회
    public List<LocalDate> getUserDetailDateList(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        return userRecordRepository.getPassDate(user);
    }
}
