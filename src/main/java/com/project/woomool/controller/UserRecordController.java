package com.project.woomool.controller;

import com.project.woomool.controller.request.UserRecordRequest;
import com.project.woomool.controller.response.userRecordResponse.UserRecordDateListResponse;
import com.project.woomool.controller.response.userRecordResponse.UserRecordListResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.service.UserRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userRecord")
public class UserRecordController {
    private final UserRecordService userRecordService;

    @RequestMapping("/add")
    public void addRecord(@RequestBody UserRecordRequest request , @AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        userRecordService.addRecord(request, userDto);
    }

    @GetMapping("/getRecords")
    public ResponseEntity<UserRecordListResponse> getRecords(@AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        UserRecordListResponse response = new UserRecordListResponse(userRecordService.getRecords(userDto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserRecordListResponse> getRecordsById(@PathVariable Long userId) {
        UserRecordListResponse response = new UserRecordListResponse(userRecordService.getRecordsById(userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCalendar")
    public ResponseEntity<UserRecordDateListResponse> getCalendar(@AuthenticationPrincipal CustomOAuth2UserDTO userDto) {
        UserRecordDateListResponse response = new UserRecordDateListResponse(userRecordService.getUserDetailDateList(userDto));
        return ResponseEntity.ok(response);
    }

}
