package com.project.woomool.controller.response.team;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TeamJoinResponse {
    private String message;
    private String code;

    public TeamJoinResponse(String code) {
        this.message = "가입이 완료되었습니다.";
        this.code = code;
    }
}
