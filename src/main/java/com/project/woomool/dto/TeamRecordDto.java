package com.project.woomool.dto;

import com.project.woomool.entity.TeamRecord;
import lombok.Builder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TeamRecordDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private float amount;

    public static TeamRecordDto of(TeamRecord record) {
        return TeamRecordDto.builder()
                .date(record.getDate())
                .amount(record.getAmount())
                .build();

    }
}
