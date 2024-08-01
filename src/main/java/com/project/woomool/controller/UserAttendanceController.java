package com.project.woomool.controller;

import com.project.woomool.controller.request.UserRecordRequest;
import com.project.woomool.controller.response.userAttendance.UserAttendanceListResponse;
import com.project.woomool.controller.response.userRecordResponse.AddedResponse;
import com.project.woomool.controller.response.userRecordResponse.UserRecordDateListResponse;
import com.project.woomool.controller.response.userRecordResponse.UserRecordListResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.service.UserAttendanceService;
import com.project.woomool.service.UserRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userAttendance")
public class UserAttendanceController {
    private final UserAttendanceService userAttendanceService;

    @GetMapping("/get")
    public ResponseEntity<UserAttendanceListResponse> getAttendance(@AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        UserAttendanceListResponse response = new UserAttendanceListResponse(userAttendanceService.getAttendance(userDto));
        return ResponseEntity.ok(response);
    }

}
