package com.project.woomool.controller.response.userRecordResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserRecordDateListResponse {

    private List<LocalDate> passDates;

    public UserRecordDateListResponse(List<LocalDate> passDates) {
        this.passDates = passDates;
    }
}