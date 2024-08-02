package com.project.woomool.controller.response.team;

import com.project.woomool.dto.TeamDetailDto;
import com.project.woomool.dto.TeamDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class EveryTeamResponse {
    private List<TeamDto> teams;

    public EveryTeamResponse (List<TeamDto> dtos) {
        this.teams = dtos;
    }
}
