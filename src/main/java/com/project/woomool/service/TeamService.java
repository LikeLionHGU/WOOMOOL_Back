package com.project.woomool.service;

import com.project.woomool.controller.request.TeamJoinRequest;
import com.project.woomool.controller.request.TeamRequest;
import com.project.woomool.controller.response.ErrorResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.TeamDto;
import com.project.woomool.entity.Team;
import com.project.woomool.entity.TeamDetail;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.exception.AlreadyJoinedException;
import com.project.woomool.exception.TeamCodeNotExistsException;
import com.project.woomool.repository.TeamDetailRepository;
import com.project.woomool.repository.TeamRepository;
import com.project.woomool.repository.UserDetailRepository;
import com.project.woomool.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.rmi.AlreadyBoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamDetailRepository  teamDetailRepository;
    private final UserDetailRepository userDetailRepository;

    public TeamDto createTeam(TeamRequest request, CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);
        float initAmount = userDetail.getRecommendation();
        float currentAmount = userDetail.getTodayTotal();

        String groupCode = generateTeamCode(request.getName());

        while(teamRepository.existsTeamByCode(groupCode)){
            groupCode = generateTeamCode(request.getName());
        }


        TeamDto groupDto = TeamDto.of(request.getName(), initAmount, currentAmount, groupCode);

        teamRepository.save(Team.of(groupDto));
        return groupDto;
    }

    @Transactional
    public void joinTeam(TeamJoinRequest request, CustomOAuth2UserDTO userDto){


        if(!teamRepository.existsTeamByCode(request.getTeamCode())){
            throw new TeamCodeNotExistsException();
        }
            User user = userRepository.findByEmail(userDto.getEmail());
            Team team = teamRepository.findTeamByCode(request.getTeamCode());
            if(teamDetailRepository.existsByUser(user) && teamDetailRepository.existsByTeam(team)){
                throw new AlreadyJoinedException();
            }

            UserDetail userDetail = userDetailRepository.findByUser(user);
            float amount = userDetail.getRecommendation();
            float currentAmount = userDetail.getTodayTotal();

            TeamDetail teamDetail = TeamDetail.of(user, team);
            teamDetailRepository.save(teamDetail);

            team.updateByJoin(amount, currentAmount);
        }

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
