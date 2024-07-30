package com.project.woomool.controller.response.userRecordResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AddedResponse {
    private String message;

    public AddedResponse(String message) {
        this.message = message;
    }
}