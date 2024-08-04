package com.project.woomool.service;

import com.project.woomool.controller.request.TeamCodeRequest;
import com.project.woomool.controller.request.TeamJoinRequest;
import com.project.woomool.controller.request.TeamRequest;
import com.project.woomool.controller.response.ErrorResponse;
import com.project.woomool.dto.*;
import com.project.woomool.entity.Team;
import com.project.woomool.entity.TeamDetail;
import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import com.project.woomool.exception.AlreadyJoinedException;
import com.project.woomool.exception.GroupNameExistsException;
import com.project.woomool.exception.NickNameExistsException;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamDetailRepository  teamDetailRepository;
    private final UserDetailRepository userDetailRepository;

    public TeamDto createTeam(TeamRequest request, CustomOAuth2UserDTO userDto, String imageURL) {
        if(teamRepository.existsTeamByName(request.getName())){
            throw new GroupNameExistsException();
        }


        User user = userRepository.findByEmail(userDto.getEmail());
        UserDetail userDetail = userDetailRepository.findByUser(user);
        float initAmount = userDetail.getRecommendation();

        String groupCode = generateTeamCode(request.getName());

        while(teamRepository.existsTeamByCode(groupCode)){
            groupCode = generateTeamCode(request.getName());
        }


        TeamDto groupDto = TeamDto.of(request.getName(), initAmount, groupCode,imageURL);
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

            team.updateByJoin(amount, userDetail.getTodayTotal());
            teamDetailRepository.save(teamDetail);
            teamRepository.save(team);
        }


    public TeamDto getGroupInfo(Long groupId) {
        Team team = teamRepository.findTeamById(groupId);
        return TeamDto.of(team);
    }


    public TeamDto getGroupByCode(String code) {
        Team team = teamRepository.findTeamByCode(code);

        return TeamDto.of(team);
    }
    @Transactional
    public TeamDto exitGroup(Long groupId, CustomOAuth2UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        Team team = teamRepository.findTeamById(groupId);
        TeamDetail teamDetail = teamDetailRepository.findTeamDetailByTeamAndUser(team, user);
        UserDetail userDetail = userDetailRepository.findByUser(user);

        if(teamDetail.getPastWaterRecommendation()==0){
            team.minusTotal(userDetail.getTodayTotal());
            team.minusRecommendation(teamDetail.getPastWaterRecommendation() + ((7 - team.getDateCount()) * userDetail.getRecommendation()));
            team.minusPeople();
        }else {
            team.minusPastRecommendation(teamDetail.getPastWaterRecommendation());
            team.minusTodayRecommendation(userDetail.getRecommendation());
            team.minusTotal(teamDetail.getWaterAmount()+userDetail.getTodayTotal());
            team.minusRecommendation(teamDetail.getPastWaterRecommendation() + ((7 - team.getDateCount()) * userDetail.getRecommendation()));
            team.minusPeople();
        }
        teamDetailRepository.deleteTeamDetailById(teamDetail.getId());
        teamRepository.save(team);

        //여기에 팀과 유저가 없을 시 exception

        return TeamDto.of(team);
    }


    public List<TeamUserDto> getGroupUsers(String code) {
        Team team = teamRepository.findTeamByCode(code);
        List<TeamDetail> teamDetails = teamDetailRepository.findAllByTeam(team);

        List<TeamUserDto> usersInfo = new ArrayList<>();
        for(TeamDetail teamDetail: teamDetails){
            User user = teamDetail.getUser();
            float waterAmount = teamDetail.getWaterAmount();
//            float waterAmount = teamRepository.sumAmountByUserAndTeam(user,team);

            usersInfo.add(TeamUserDto.of(user, waterAmount));

        }

        return usersInfo;
    }

//    public List<TeamDetailDto> getAllTeam() {
//        List<TeamDetail> teamDetails = teamDetailRepository.findAll();
//
//        return teamDetails.stream()
//                .map(TeamDetailDto::of)
//                .collect(Collectors.toList());
//    }

    public List<TeamDto> getAllTeam(){
        List<Team> teams = teamRepository.findAll();

        return teams. stream()
                .map(TeamDto::of)
                .collect(Collectors.toList());
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

    @Scheduled(cron = "00 59 23 * * *")
    @Transactional
    public void autoUpdateRestDay() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            if(team.getDateCount()>=6){

                team.setRecommendation(team.getPastRecommendation()+(team.getTodayRecommendation()*(7-team.getDateCount())));

                if(team.getGroupTotal()>=(team.getRecommendation()*0.8)){
                    team.plusCompleteLevel();
                }
                List<TeamDetail> teamDetails = teamDetailRepository.findAllByTeam(team);
                for(TeamDetail teamDetail:teamDetails){
                    teamDetail.setWaterAmount(0);
                    teamDetail.setPastWaterRecommendation(0);
                    teamDetailRepository.save(teamDetail);
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
