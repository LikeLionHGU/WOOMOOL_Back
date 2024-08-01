package com.project.woomool.controller.response.userAttendance;

import com.project.woomool.dto.UserAttendanceDto;
import com.project.woomool.dto.UserRecordDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserAttendanceListResponse {

    private List<UserAttendanceDto> userAttendanceDtos;

    public UserAttendanceListResponse(List<UserAttendanceDto> userAttendanceDtos) {
        this.userAttendanceDtos = userAttendanceDtos;
    }
}
