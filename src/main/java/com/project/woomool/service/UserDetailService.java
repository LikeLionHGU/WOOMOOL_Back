package com.project.woomool.service;

import com.project.woomool.controller.request.UserDetailRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserDetailDto;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.repository.UserDetailRepository;
import com.project.woomool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;

    public UserDetailDto addDetail(UserDetailRequest request , CustomOAuth2UserDTO userDto) {

        float bmi = (request.getWeight() / (request.getHeight() * request.getHeight()));
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = UserDetail.of(request,bmi,user);

        userDetailRepository.save(userDetail);
        return UserDetailDto.of(userDetail);
    }

}
