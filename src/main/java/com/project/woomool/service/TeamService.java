package com.project.woomool.service;

import com.project.woomool.controller.request.TeamJoinRequest;
import com.project.woomool.controller.request.TeamRequest;
import com.project.woomool.controller.response.ErrorResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.TeamDetailDto;
import com.project.woomool.dto.TeamDto;
import com.project.woomool.dto.UserDetailDto;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.rmi.AlreadyBoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

        String groupCode = generateTeamCode(request.getName());

        while(teamRepository.existsTeamByCode(groupCode)){
            groupCode = generateTeamCode(request.getName());
        }


        TeamDto groupDto = TeamDto.of(request.getName(), initAmount, groupCode);
        Team team = Team.of(groupDto);

        teamRepository.save(team);
        return TeamDto.of(team);
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

            TeamDetail teamDetail = TeamDetail.of(user, team);

            team.updateByJoin(amount);
            teamDetailRepository.save(teamDetail);
            teamRepository.save(team);
        }


    public TeamDto getGroupInfo(Long groupId) {
        Team team = teamRepository.findTeamById(groupId);
        return TeamDto.of(team);
    }

//    public List<TeamDetailDto> getGroupList(CustomOAuth2UserDTO userDto) {
//        User user = userRepository.findByEmail(userDto.getEmail());
//        List<TeamDetail> teamDetails = teamDetailRepository.findAllByUser(user);
//
//        List<TeamDetailDto> teamDetailDtos = new ArrayList<>();
//
//        for (TeamDetail teamDetail : teamDetails) {
//            teamDetailDtos.add(TeamDetailDto.of(teamDetail));
//        }
//
//        return teamDetailDtos;
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

    @Scheduled(cron = "59 59 23 * * *")
    @Transactional
    public void autoUpdateRestDay() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            if(team.getDateCount()>=6){

                team.setRecommendation(team.getPastRecommendation()+(team.getTodayRecommendation()*(7-team.getDateCount())));

                if(team.getGroupTotal()>=(team.getRecommendation()*0.8)){
                    team.plusCompleteLevel();
                }

                team.setPastRecommendation(0);
                team.setGroupTotal(0);
                team.setDateCount(0);
                team.setTodayRecommendation(0);

            }else {
                team.setRecommendation(team.getPastRecommendation()+(team.getTodayRecommendation()*(7-team.getDateCount())));
                team.plusPastRecommendation(team.getTodayRecommendation());
                team.plusDateCount();
                team.setTodayRecommendation(0);
            }
            teamRepository.save(team);
        }
    }



}
