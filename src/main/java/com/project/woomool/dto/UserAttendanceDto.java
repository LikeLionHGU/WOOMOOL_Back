package com.project.woomool.dto;

import com.project.woomool.entity.UserDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserAttendanceDto {


    private LocalDate date;
    private float recommendation;
    private float drankWater;
    private boolean hasDrankToday;
    private boolean warnDrankToday;
    private int week;
    private int dateCount;
    private boolean attendance;

    public static UserAttendanceDto of(UserDetail userDetail) {
        return UserAttendanceDto.builder()
            .date(LocalDate.now())
            .recommendation(userDetail.getRecommendation())
                .drankWater(userDetail.getTodayTotal())
                .hasDrankToday(userDetail.isHasDrankToday())
                .warnDrankToday(userDetail.isWarnDrankToday())
                .week(userDetail.getWeek())
                .dateCount(userDetail.getWeekDate())
                .attendance(userDetail.isAttendance())
            .build();
    }

}
