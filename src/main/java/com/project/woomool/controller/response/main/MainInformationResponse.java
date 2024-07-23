package com.project.woomool.controller.response.main;

import com.project.woomool.dto.MainDto;
import com.project.woomool.dto.TeamDto;

public class MainInformationResponse {

    private int memberCount;
    private int groupCount;
    private float totalWater;

    public MainInformationResponse(MainDto dto) {
        this.memberCount = dto.getMemberCount();
        this.groupCount = dto.getGroupCount();
        this.totalWater = dto.getTotalWater();
    }
}
