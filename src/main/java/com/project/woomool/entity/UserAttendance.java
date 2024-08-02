package com.project.woomool.entity;

import com.project.woomool.dto.UserAttendanceDto;
import com.project.woomool.dto.UserRecordDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    private LocalDate date;
    private float recommendation;
    private float drankWater;
    private boolean hasDrankToday;
    private boolean warnDrankToday;
    private int week;
    private int dateCount;
    private boolean attendance;


    private float weekBeforeWater;

    private float weekBeforeRecommendation;


    public void addWeekBeforeWater(float amount){this.weekBeforeWater+=amount;}

    public void addWeekBeforeRecommendation(float amount){this.weekBeforeRecommendation+=amount;}
    public static UserRecord of(UserRecordDto dto,User user){
        return UserRecord.builder()
                .user(user)
                .date(dto.getDate())
                .amount(dto.getAmount())
                .build();
    }

    public static UserAttendance of(UserAttendanceDto dto, User user) {
        return UserAttendance.builder()
                .user(user)
                .date(dto.getDate())
                .recommendation(dto.getRecommendation())
                .drankWater(dto.getDrankWater())
                .hasDrankToday(dto.isHasDrankToday())
                .warnDrankToday(dto.isWarnDrankToday())
                .week(dto.getWeek())
                .dateCount(dto.getDateCount())
                .attendance(dto.isAttendance())
                .build();
    }



}
