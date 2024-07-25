package com.project.woomool.controller.response.teamDetail;

import com.project.woomool.dto.TeamDetailDto;
import com.project.woomool.dto.TeamDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TeamListResponse {
    private List<TeamDetailDto> teams;

    public TeamListResponse(List<TeamDetailDto> dtos) {
        this.teams = dtos;
    }
}
