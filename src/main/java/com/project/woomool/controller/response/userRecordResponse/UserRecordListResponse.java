package com.project.woomool.controller.response.userRecordResponse;

import com.project.woomool.dto.UserRecordDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserRecordListResponse {

    private List<UserRecordDto> userRecordDtos;

    public UserRecordListResponse(List<UserRecordDto> userRecordDtos) {
        this.userRecordDtos = userRecordDtos;
    }
}
