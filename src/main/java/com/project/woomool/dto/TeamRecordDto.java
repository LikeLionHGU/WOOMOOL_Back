package com.project.woomool.dto;

import com.project.woomool.entity.TeamRecord;
import lombok.Builder;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TeamRecordDto {

    private LocalDate date;
    private float amount;

    public static TeamRecordDto of(TeamRecord record) {
        return TeamRecordDto.builder()
                .date(record.getDate())
                .amount(record.getAmount())
                .build();

    }
}
