package com.project.woomool.dto;


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
    private float recommendation;
    private float groupTotal;

    public static TeamDto of(String name , float recommendation , String code){

        return TeamDto.builder()
            .name(name)
            .recommendation((7*recommendation))
            .dateCount(0)
            .code(code)
            .build();
    }

}
