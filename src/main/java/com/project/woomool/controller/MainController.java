package com.project.woomool.controller;

import com.project.woomool.controller.response.main.MainInformationResponse;
import com.project.woomool.controller.response.userRecordResponse.UserRecordListResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.MainDto;
import com.project.woomool.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;

    @GetMapping("/getInfo")
    public ResponseEntity<MainInformationResponse> getMainInfo() {
        MainDto dto = mainService.getMainInfo();
        MainInformationResponse response = new MainInformationResponse(dto);
        return ResponseEntity.ok(response);
    }
}
