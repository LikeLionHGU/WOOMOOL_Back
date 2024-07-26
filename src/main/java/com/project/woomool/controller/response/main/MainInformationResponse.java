package com.project.woomool.controller.response.main;

import com.project.woomool.dto.MainDto;
import com.project.woomool.dto.TeamDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MainInformationResponse {

    private long memberCount;
    private long groupCount;
    private float totalWater;

    public MainInformationResponse(MainDto dto) {
        this.memberCount = dto.getMemberCount();
        this.groupCount = dto.getGroupCount();
        this.totalWater = dto.getTotalWater();
    }
}
