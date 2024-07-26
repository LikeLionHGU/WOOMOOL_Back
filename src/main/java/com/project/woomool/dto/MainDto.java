package com.project.woomool.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MainDto {

    private long memberCount;
    private long groupCount;
    private float totalWater;

    public static MainDto of(long memberCount , long groupCount , float totalWater){

        return MainDto.builder()
                .memberCount(memberCount)
                .groupCount(groupCount)
                .totalWater(totalWater)
                .build();
    }
}
