package com.project.woomool.controller;

import com.project.woomool.controller.request.UserNickNameRequest;
import com.project.woomool.controller.response.teamDetail.TeamListResponse;
import com.project.woomool.controller.response.user.OnlyUserResponse;
import com.project.woomool.controller.response.user.UserResponse;
import com.project.woomool.controller.response.userDetail.UserDetailResponse;
import com.project.woomool.controller.response.userRecordResponse.AddedResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/nickname")
    public ResponseEntity<UserResponse> addNickName(@RequestBody UserNickNameRequest request, @AuthenticationPrincipal CustomOAuth2UserDTO userDto){
        userService.addNickName(request,userDto);
        String result = "닉네임"+ request.getNickName() +"가 추가되었습니다.";

        UserResponse response = new UserResponse(request.getNickName(),userDto.getUsername(),result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/nicknameCheck")
    public ResponseEntity<AddedResponse> nickNameCheck(@RequestBody UserNickNameRequest request){
        AddedResponse response = new AddedResponse(userService.checkNickName(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public  ResponseEntity<OnlyUserResponse> getUser(@AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        OnlyUserResponse response = new OnlyUserResponse(userService.getUser(userDto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getGroups")
    public  ResponseEntity<TeamListResponse> getTeams(@AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        TeamListResponse response = new TeamListResponse(userService.getTeamsByUser(userDto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getGroupsById/{userId}")
    public  ResponseEntity<TeamListResponse> getTeamsById(@PathVariable Long userId) {
        TeamListResponse response = new TeamListResponse(userService.getTeamsByUserId(userId));
        return ResponseEntity.ok(response);
    }







}
