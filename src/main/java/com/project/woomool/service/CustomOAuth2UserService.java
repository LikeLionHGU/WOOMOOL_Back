package com.project.woomool.service;


import com.project.woomool.controller.response.loginResponse.GoogleResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.OAuth2Response;
import com.project.woomool.dto.UserDTO;
import com.project.woomool.entity.User;
import com.project.woomool.repository.UserRepository;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service

public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        List<String> admin = new ArrayList<>(Arrays.asList("hello"));
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        OAuth2Response oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        String name = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        User existData = userRepository.findByEmail(oAuth2Response.getEmail());

        String role = null;

        if (existData == null) {
            User userEntity = new User();
            userEntity.setUsername(oAuth2Response.getName());
            userEntity.setEmail(oAuth2Response.getEmail());
            if (admin.contains(oAuth2Response.getEmail())) {
                userEntity.setRole("ROLE_ADMIN");
            } else {
                userEntity.setRole("ROLE_USER");
            }
            userEntity.setName(name);
            userRepository.save(userEntity);


            UserDTO userDTO = new UserDTO();
            userDTO.setName(name);
            userDTO.setUsername(oAuth2Response.getName());
            userDTO.setEmail(oAuth2Response.getEmail());
            if (admin.contains(oAuth2Response.getEmail())) {
                userDTO.setRole("ROLE_ADMIN");
            } else {
                userDTO.setRole("ROLE_USER");
            }

            return new CustomOAuth2UserDTO(userDTO);

        } else {

            existData.setUsername(oAuth2Response.getName());
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            role = existData.getRole();

//      userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setRole(existData.getRole());
            userDTO.setName(existData.getName());
            userDTO.setEmail(existData.getEmail());

            return new CustomOAuth2UserDTO(userDTO);

        }
    }
}
