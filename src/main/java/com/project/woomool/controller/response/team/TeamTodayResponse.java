package com.project.woomool.controller.response.team;

import com.project.woomool.dto.TeamDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TeamTodayResponse {

    private float recommendation;
    private float todayGroupTotal;
    private boolean passOrNot;

    public TeamTodayResponse(float recommendation, float todayGroupTotal) {
        this.recommendation = recommendation;
        this.todayGroupTotal = todayGroupTotal;
        this.passOrNot = (recommendation*0.8)<=todayGroupTotal;
    }

}
