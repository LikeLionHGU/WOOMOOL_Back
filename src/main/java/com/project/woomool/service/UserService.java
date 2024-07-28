package com.project.woomool.service;

import com.project.woomool.controller.request.UserNickNameRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserDTO;
import com.project.woomool.dto.UserDetailDto;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.exception.NickNameExistsException;
import com.project.woomool.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void addNickName(UserNickNameRequest request, CustomOAuth2UserDTO userDto){

        if(userRepository.existsByNickName(request.getNickName())){
            throw new NickNameExistsException();
        }

        User user = userRepository.findByEmail(userDto.getEmail());

        user.addNickName(request);
    }

    public UserDTO getUser(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        return UserDTO.of(user);
    }

}
