package com.project.woomool.controller.response.user;

import com.project.woomool.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class OnlyUserResponse {

    private Long id;
    private String userName;
    private String nickName;
    private String email;


    public OnlyUserResponse(UserDTO dto) {
        this.id = dto.getId();
        this.userName = dto.getUsername();
        this.nickName = dto.getNickName();
        this.email = dto.getEmail();
    }
}