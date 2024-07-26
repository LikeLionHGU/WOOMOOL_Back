package com.project.woomool.controller.response.userRecordResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserRecordResponse {

    private Date date;
    private float amount;

    public UserRecordResponse(Date date, float amount) {
        this.date = date;
        this.amount = amount;
    }



}
