package com.project.woomool.controller.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserResponse {

    private String userName;
    private String nickName;
    private String result;


    public UserResponse(String nickName, String userName,String result) {
        this.userName = userName;
        this.nickName = nickName;
        this.result = result;
    }
}
