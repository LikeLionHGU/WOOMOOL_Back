package com.project.woomool.service;

import com.project.woomool.controller.request.UserNickNameRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.TeamDetailDto;
import com.project.woomool.dto.UserDTO;
import com.project.woomool.dto.UserDetailDto;
import com.project.woomool.entity.TeamDetail;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.exception.NickNameExistsException;
import com.project.woomool.repository.TeamDetailRepository;
import com.project.woomool.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TeamDetailRepository teamDetailRepository;

    @Transactional
    public void addNickName(UserNickNameRequest request, CustomOAuth2UserDTO userDto){

        if(userRepository.existsByNickName(request.getNickName())){
            throw new NickNameExistsException();
        }

        User user = userRepository.findByEmail(userDto.getEmail());

        user.addNickName(request);
    }

    public String checkNickName(UserNickNameRequest request){
        String result = "사용가능";
        if(userRepository.existsByNickName(request.getNickName())){
            result = "사용불가";
        }

        return result;
    }

    public UserDTO getUser(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        return UserDTO.of(user);
    }

    public List<TeamDetailDto> getTeamsByUser(CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(user);

        return teamDetails.stream()
                .map(TeamDetailDto::of)
                .collect(Collectors.toList());
    }

    public List<TeamDetailDto> getTeamsByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(user);

        return teamDetails.stream()
                .map(TeamDetailDto::of)
                .collect(Collectors.toList());
    }


}
