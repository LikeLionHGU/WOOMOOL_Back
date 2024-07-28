package com.project.woomool.entity;

import com.project.woomool.dto.TeamDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;
    private int completeLevel; //팀의 달성 횟수
    private int dateCount; //일주일중 지난 날
    private float pastRecommendation; //지난 검증된 물의 양
    private float todayRecommendation; //그룹 별 하루의 추천양
    private float recommendation;
    private float groupTotal;

    public static Team of(TeamDto dto) {
        return Team.builder()
            .name(dto.getName())
            .code(dto.getCode())
            .pastRecommendation(dto.getPastRecommendation())
            .todayRecommendation(dto.getTodayRecommendation())
            .completeLevel(dto.getCompleteLevel())
            .dateCount(dto.getDateCount())
            .recommendation(dto.getRecommendation())
            .groupTotal(dto.getGroupTotal())
            .build();
    }

    public void updateTotal(float amount) {
        this.groupTotal += amount;
    }

    public void updateTodayRecommendation(float amount) {
        this.todayRecommendation += amount;
    }

    public void plusPastRecommendation(float amount) {
        this.pastRecommendation += amount;
    }

    public void plusDateCount(){this.dateCount++;}

    public void plusCompleteLevel(){this.completeLevel++;}

    public void updateByJoin(float recommendation ) {
            this.recommendation += ((7-dateCount)*recommendation);
        }
    }


