package com.project.woomool.dto;


import com.project.woomool.entity.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamDto {

    private String name;
    private String code;
    private int completeLevel; //팀의 달성 횟수
    private int dateCount;
    private float finalRecommendation;
    private float recommendation;
    private float groupTotal;

    public static TeamDto of(String name , float recommendation , String code){

        return TeamDto.builder()
            .name(name)
            .recommendation(0)
            .finalRecommendation(0)
            .completeLevel(0)
            .dateCount(0)
            .groupTotal(0)
            .code(code)
            .build();
    }

    public static TeamDto of(Team team){

        return TeamDto.builder()
                .name(team.getName())
                .recommendation(team.getRecommendation())
                .finalRecommendation(team.getFinalRecommendation())
                .groupTotal(team.getGroupTotal())
                .completeLevel(team.getCompleteLevel())
                .dateCount(team.getDateCount())
                .code(team.getCode())
                .build();
    }

}
