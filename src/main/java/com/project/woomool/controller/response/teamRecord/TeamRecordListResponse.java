package com.project.woomool.controller.response.teamRecord;

import com.project.woomool.dto.TeamDetailDto;
import com.project.woomool.dto.TeamRecordDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TeamRecordListResponse {

    private List<TeamRecordDto> teamRecordDtos;

    public TeamRecordListResponse(List<TeamRecordDto> dtos) {
        this.teamRecordDtos = dtos;
    }
}
