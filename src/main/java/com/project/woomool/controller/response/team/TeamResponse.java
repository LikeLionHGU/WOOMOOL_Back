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
    private int completeLevel; //팀의 달성 횟수
    private int dateCount; //일주일중 지난 날

    public TeamResponse(TeamDto dto) {
        this.name = dto.getName();
        this.code = dto.getCode();
        this.recommendation = dto.getRecommendation();
        this.groupTotal = dto.getGroupTotal();
        this.completeLevel = dto.getCompleteLevel();
        this.dateCount = dto.getDateCount();
    }

}
