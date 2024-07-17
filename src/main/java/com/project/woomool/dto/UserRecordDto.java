package com.project.woomool.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
public class UserRecordDto {

    private LocalDate date;
    private float amount;

    public static UserRecordDto of(float amount) {
        return UserRecordDto.builder()
            .date(LocalDate.now())
            .amount(amount)
            .build();
    }

}
