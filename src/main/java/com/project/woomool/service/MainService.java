package com.project.woomool.service;

import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.MainDto;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.repository.TeamDetailRepository;
import com.project.woomool.repository.TeamRepository;
import com.project.woomool.repository.UserDetailRepository;
import com.project.woomool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamDetailRepository teamDetailRepository;
    private final UserDetailRepository userDetailRepository;

    public MainDto getMainInfo() {
        MainDto mainDto = MainDto.of(999,999, 20.0F);
        return mainDto;
    }
}
