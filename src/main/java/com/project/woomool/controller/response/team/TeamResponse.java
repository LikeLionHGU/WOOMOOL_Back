package com.project.woomool.controller.response.team;

import com.project.woomool.dto.TeamDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TeamResponse {
    private String name;
    private String code;
    private float recommendation;
    private float groupTotal;

    public TeamResponse(TeamDto dto) {
        this.name = dto.getName();
        this.code = dto.getCode();
        this.recommendation = dto.getRecommendation();
        this.groupTotal = dto.getGroupTotal();
    }

}
