package com.project.woomool.controller;

import com.project.woomool.controller.request.UserRecordRequest;
import com.project.woomool.controller.response.userDetail.UserDetailResponse;
import com.project.woomool.controller.response.userRecordResponse.UserRecordListResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.service.UserRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userRecord")
public class UserRecordController {
    private final UserRecordService userRecordService;

    @RequestMapping("/add")
    public void addRecord(@RequestBody UserRecordRequest request , @AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        userRecordService.addRecord(request, userDto);
    }

//    @GetMapping("/getRecords")
//    public ResponseEntity<UserRecordListResponse> getRecords(@AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
//        return ResponseEntity.ok(userRecordService.getRecords(userDto));
//    }

}
