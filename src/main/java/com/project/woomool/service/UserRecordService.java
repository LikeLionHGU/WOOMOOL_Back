package com.project.woomool.service;

import com.project.woomool.controller.request.UserRecordRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.entity.UserRecord;
import com.project.woomool.repository.UserDetailRepository;
import com.project.woomool.repository.UserRecordRepository;
import com.project.woomool.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRecordService {

    private final UserRecordRepository userRecordRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addRecord(UserRecordRequest request, CustomOAuth2UserDTO userDto) {
            User user = userRepository.findByEmail(userDto.getEmail());
            UserDetail userDetail = userDetailRepository.findByUser(user);
            UserRecordDto dto =  UserRecordDto.of(request.getAmount());
            UserRecord userRecord = UserRecord.of(dto,user);
            userDetail.updateTotal(request.getAmount());
            userRecordRepository.save(userRecord);
    }



}
