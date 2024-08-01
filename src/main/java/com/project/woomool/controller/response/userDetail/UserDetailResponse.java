package com.project.woomool.controller.response.userDetail;

import com.project.woomool.dto.UserDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserDetailResponse {

    private float weight;
    private float height;
    private float bmi;
    private float recommendation;
    private float todayTotal;
    private int hasDrankLevel;
    private boolean hasDrankToday;
    private boolean warnDrankToday;
    private int cup;
    private int week;
    private int weekDate;
    private boolean attendance;

    public UserDetailResponse (UserDetailDto dto) {
            this.weight = dto.getWeight();
            this.height = dto.getHeight();
            this.bmi = dto.getBmi();
            this.todayTotal = dto.getTodayTotal();
            this.recommendation = dto.getRecommendation();
            this.hasDrankLevel = dto.getHasDrankLevel();
            this.hasDrankToday = dto.isHasDrankToday();
            this.warnDrankToday = dto.isWarnDrankToday();
            this.cup = dto.getCup();
            this.week = dto.getWeek();
            this.weekDate = dto.getWeekDate();
            this.attendance = dto.isAttendance();
    }
}
