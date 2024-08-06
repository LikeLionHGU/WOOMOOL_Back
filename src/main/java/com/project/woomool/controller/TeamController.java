package com.project.woomool.controller;

import com.project.woomool.controller.request.TeamCodeRequest;
import com.project.woomool.controller.request.TeamJoinRequest;
import com.project.woomool.controller.request.TeamRequest;
import com.project.woomool.controller.response.team.*;
import com.project.woomool.controller.response.teamDetail.TeamListResponse;
import com.project.woomool.controller.response.teamRecord.TeamRecordListResponse;
import com.project.woomool.controller.response.teamRecord.TeamRecordResponse;
import com.project.woomool.controller.response.userDetail.UserDetailResponse;
import com.project.woomool.controller.response.userRecordResponse.AddedResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.TeamDto;
import com.project.woomool.entity.TeamRecord;
import com.project.woomool.exception.TeamCodeNotExistsException;
import com.project.woomool.service.S3Uploader;
import com.project.woomool.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;
    private final S3Uploader s3Uploader;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TeamResponse> createTeam(@RequestParam(name = "image", required = false) MultipartFile image, TeamRequest request, @AuthenticationPrincipal CustomOAuth2UserDTO userDto) throws IOException {
        String imageUrl = s3Uploader.upload(image,"example");
        System.out.println("hello" + request.getName());
        TeamDto dto = teamService.createTeam(request, userDto,imageUrl);

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

    @GetMapping("/getByCode/{groupCode}")
    public  ResponseEntity<TeamResponse> getGroupInfoByCode(@PathVariable String groupCode) {
        TeamResponse response = new TeamResponse(teamService.getGroupByCode(groupCode));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTodayByCode/{groupCode}")
    public  ResponseEntity<TeamTodayResponse> getTodayGroupInfoByCode(@PathVariable String groupCode) {
        TeamTodayResponse response = new TeamTodayResponse(teamService.getTodayGroupByCode(groupCode), teamService.getGroupByCode(groupCode).getRecommendation());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/UsersByCode/{groupCode}")
    public  ResponseEntity<TeamUserResponse> getGroupUserByCode(@PathVariable String groupCode) {
        TeamUserResponse response = new TeamUserResponse(teamService.getGroupUsers(groupCode));
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

    @PatchMapping("/exit/{groupId}")
    public ResponseEntity<TeamResponse> exitTeam(@PathVariable Long groupId, @AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        TeamDto teamDto = teamService.exitGroup(groupId, userDto);

        TeamResponse response = new TeamResponse(teamDto);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/record/{groupdCode}")
    public ResponseEntity<TeamRecordListResponse> getGroupRecord(@PathVariable String groupdCode) {
        TeamRecordListResponse response = new TeamRecordListResponse(teamService.getGroupTotalByDay(groupdCode));
        return ResponseEntity.ok(response);
    }



    }
