package com.project.woomool.controller;

import com.project.woomool.controller.request.UserDetailRequest;
import com.project.woomool.controller.response.userDetail.UserDetailResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserDetailDto;
import com.project.woomool.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userDetail")
public class UserDetailController {

    private final UserDetailService userDetailService;

    @PostMapping("/add")
    public ResponseEntity<UserDetailResponse> addDetail(@RequestBody UserDetailRequest request, @AuthenticationPrincipal CustomOAuth2UserDTO userDto) {

        UserDetailDto dto = userDetailService.addDetail(request, userDto);
        UserDetailResponse response = new UserDetailResponse(dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public  ResponseEntity<UserDetailResponse> getDetail(@AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        UserDetailResponse response = new UserDetailResponse(userDetailService.getUserDetail(userDto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public  ResponseEntity<UserDetailResponse> getDetailByUserId(@PathVariable Long userId) {
        UserDetailResponse response = new UserDetailResponse(userDetailService.getUserDetailByUserId(userId));
        return ResponseEntity.ok(response);
    }





}
