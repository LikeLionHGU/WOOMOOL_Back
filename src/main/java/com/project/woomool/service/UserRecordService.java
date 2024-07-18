package com.project.woomool.service;

import com.project.woomool.controller.request.UserRecordRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserRecordDto;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.entity.UserRecord;
import com.project.woomool.repository.TeamRepository;
import com.project.woomool.repository.UserDetailRepository;
import com.project.woomool.repository.UserRecordRepository;
import com.project.woomool.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRecordService {

    private final UserRecordRepository userRecordRepository;
    private final UserDetailRepository userDetailRepository;
    private final TeamRepository teamRepository;
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

    public List<UserRecordDto> getRecords(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);
        List<UserRecordDto> records = userRecordRepository.findAllByUserId(userDetail.getId());
        return records;
    }

    public List<UserRecordDto> getRecordsById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UserDetail userDetail = userDetailRepository.findByUser(user);
        List<UserRecordDto> records = userRecordRepository.findAllByUserId(userDetail.getId());
        return records;
    }



}
