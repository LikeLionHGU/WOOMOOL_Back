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
    private float recommendation;
    private float groupTotal;

    public static TeamDto of(String name , float recommendation , float groupTotal , String code){

        return TeamDto.builder()
            .name(name)
            .recommendation(recommendation)
            .groupTotal(groupTotal)
            .code(code)
            .build();
    }

}
