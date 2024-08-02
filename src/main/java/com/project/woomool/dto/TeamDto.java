package com.project.woomool.dto;


import com.project.woomool.entity.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeamDto {


    private Long TeamId;
    private String name;
    private String code;
    private int completeLevel; //팀의 달성 횟수
    private int dateCount;
    private float pastRecommendation;
    private float todayRecommendation; //그룹 별 하루의 추천양
    private float recommendation;
    private float groupTotal;
    private int peopleCount;

    public static TeamDto of(String name , float recommendation , String code){

        return TeamDto.builder()
            .name(name)
            .recommendation(0)
            .pastRecommendation(0)
            .todayRecommendation(0)
            .completeLevel(0)
            .dateCount(0)
            .groupTotal(0)
            .code(code)
            .build();
    }

    public static TeamDto of(Team team){

        return TeamDto.builder()
                .TeamId(team.getId())
                .name(team.getName())
                .recommendation(team.getRecommendation())
                .pastRecommendation(team.getPastRecommendation())
                .todayRecommendation(team.getTodayRecommendation())
                .groupTotal(team.getGroupTotal())
                .peopleCount(team.getPeopleCount())
                .completeLevel(team.getCompleteLevel())
                .dateCount(team.getDateCount())
                .code(team.getCode())
                .build();
    }

}
