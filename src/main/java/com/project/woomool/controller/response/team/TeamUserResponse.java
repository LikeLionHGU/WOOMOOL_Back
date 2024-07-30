package com.project.woomool.controller.response.team;

import com.project.woomool.dto.TeamUserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TeamUserResponse {

    private List<TeamUserDto> userInfos;

    public TeamUserResponse(List<TeamUserDto> userInfos) {
        this.userInfos = userInfos;
    }
}
