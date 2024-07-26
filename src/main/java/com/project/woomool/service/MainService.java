package com.project.woomool.service;

import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.MainDto;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.entity.UserRecord;
import com.project.woomool.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserRecordRepository userRecordRepository;

    public MainDto getMainInfo() {
        long totalUser = userRepository.count();
        long totalTeam = teamRepository.count();
        long totalWater = userRecordRepository.sumAmount();

        return MainDto.of(totalUser,totalTeam, totalWater);
    }
}
