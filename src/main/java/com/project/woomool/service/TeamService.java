package com.project.woomool.service;

import com.project.woomool.controller.request.TeamRequest;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.TeamDto;
import com.project.woomool.entity.Team;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.repository.TeamRepository;
import com.project.woomool.repository.UserDetailRepository;
import com.project.woomool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository groupRepository;
    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    public TeamDto createTeam(TeamRequest request, CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);
        float initAmount = userDetail.getRecommendation();
        float currentAmount = userDetail.getTodayTotal();

        String groupCode = generateTeamCode(request.getName());

        TeamDto groupDto = TeamDto.of(request.getName(), initAmount, currentAmount, groupCode);

        groupRepository.save(Team.of(groupDto));
        return groupDto;
    }

    //    public GroupDto joinGroup(GroupRequest request, CustomOAuth2UserDTO userDto) {
//
//
//    }

    private String generateTeamCode(String name) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(name.getBytes());
            String encoded = Base64.getEncoder().encodeToString(hash);
            return encoded.substring(0, 4).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate team code", e);
        }


    }
}
