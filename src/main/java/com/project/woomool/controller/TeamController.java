package com.project.woomool.controller;

import com.project.woomool.controller.request.TeamJoinRequest;
import com.project.woomool.controller.request.TeamRequest;
import com.project.woomool.controller.response.team.TeamJoinResponse;
import com.project.woomool.controller.response.team.TeamResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.TeamDto;
import com.project.woomool.exception.TeamCodeNotExistsException;
import com.project.woomool.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
