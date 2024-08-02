package com.project.woomool.controller;

import com.project.woomool.controller.request.TeamCodeRequest;
import com.project.woomool.controller.request.TeamJoinRequest;
import com.project.woomool.controller.request.TeamRequest;
import com.project.woomool.controller.response.team.EveryTeamResponse;
import com.project.woomool.controller.response.team.TeamJoinResponse;
import com.project.woomool.controller.response.team.TeamResponse;
import com.project.woomool.controller.response.team.TeamUserResponse;
import com.project.woomool.controller.response.teamDetail.TeamListResponse;
import com.project.woomool.controller.response.userDetail.UserDetailResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.TeamDto;
import com.project.woomool.exception.TeamCodeNotExistsException;
import com.project.woomool.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.rmi.AlreadyBoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest request, @AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        TeamDto dto = teamService.createTeam(request, userDto);

        TeamResponse response = new TeamResponse(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<TeamJoinResponse> joinTeam(@RequestBody TeamJoinRequest request, @AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        teamService.joinTeam(request, userDto);

        TeamJoinResponse response = new TeamJoinResponse(request.getTeamCode());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}")
    public  ResponseEntity<TeamResponse> getGroupInfoById(@PathVariable Long groupId) {
        TeamResponse response = new TeamResponse(teamService.getGroupInfo(groupId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByCode")
    public  ResponseEntity<TeamResponse> getGroupInfoByCode(@RequestBody TeamCodeRequest request) {
        TeamResponse response = new TeamResponse(teamService.getGroupByCode(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/UsersByCode")
    public  ResponseEntity<TeamUserResponse> getGroupUserByCode(@RequestBody TeamCodeRequest request) {
        TeamUserResponse response = new TeamUserResponse(teamService.getGroupUsers(request));
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/allGroups")
//    public  ResponseEntity<TeamListResponse> getAllGroup() {
//        TeamListResponse response = new TeamListResponse(teamService.getAllTeam());
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/allGroups")
    public  ResponseEntity<EveryTeamResponse> getAllGroup() {
        EveryTeamResponse response = new EveryTeamResponse(teamService.getAllTeam());
        return ResponseEntity.ok(response);
    }

}
