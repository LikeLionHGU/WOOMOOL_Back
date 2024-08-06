package com.project.woomool.controller.response.teamRecord;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TeamRecordResponse {
    private LocalDate date;
    private float amount;

    public TeamRecordResponse(LocalDate date, float amount) {
        this.date = date;
        this.amount = amount;
    }
}
