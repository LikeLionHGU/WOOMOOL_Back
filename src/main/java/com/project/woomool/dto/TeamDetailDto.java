package com.project.woomool.dto;

import com.project.woomool.entity.Team;
import com.project.woomool.entity.TeamDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamDetailDto {

    private String code;

    public static TeamDetailDto of(TeamDetail teamDetail){

        return TeamDetailDto.builder()
                .code(teamDetail.getTeam().getCode())
                .build();
    }

}
