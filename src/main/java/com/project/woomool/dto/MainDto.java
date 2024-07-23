package com.project.woomool.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MainDto {

    private int memberCount;
    private int groupCount;
    private float totalWater;

    public static MainDto of(int mc , int gc , float tw){

        return MainDto.builder()
                .memberCount(mc)
                .groupCount(gc)
                .totalWater(tw)
                .build();
    }
}
