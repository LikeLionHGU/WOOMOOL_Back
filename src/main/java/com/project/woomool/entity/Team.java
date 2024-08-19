package com.project.woomool.entity;

import com.project.woomool.dto.TeamDto;
import jakarta.persistence.*;
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
    private float recommendation; //위 두개를 합친걸껄?
    private float groupTotal; // 그룹이 마신 양  - 추가하는 것 완료, * 리셋 부분 미완료
    private int peopleCount;//

    @Column(length = 1000)
    private String teamImage;


    public static Team of(TeamDto dto) {
        return Team.builder()
            .name(dto.getName())
            .code(dto.getCode())
            .pastRecommendation(dto.getPastRecommendation())
            .todayRecommendation(dto.getTodayRecommendation())
            .completeLevel(dto.getCompleteLevel())
            .dateCount(dto.getDateCount())
            .recommendation(dto.getRecommendation())
                .peopleCount(dto.getPeopleCount())
            .groupTotal(dto.getGroupTotal())
                .teamImage(dto.getTeamImage())
            .build();
    }

    public void updateTotal(float amount) {
        this.groupTotal += amount;
    }

    public void minusTotal(float amount) {
        this.groupTotal -= amount;
    }

    public void updateTodayRecommendation(float amount) {
        this.todayRecommendation += amount;
    }

    public void minusTodayRecommendation(float amount) {
        this.todayRecommendation -= amount;
    }

    public void plusPastRecommendation(float amount) {
        this.pastRecommendation += amount;
    }

    public void minusPastRecommendation(float amount) {
        this.pastRecommendation -= amount;
    }

    public void plusDateCount(){this.dateCount++;}

    public void plusCompleteLevel(){this.completeLevel++;}
    public void minusRecommendation(float amount){this.recommendation -= amount;}


    public void minusPeople(){this.peopleCount--;}
    public void updateByJoin(float recommendation, float todayWater ) {
            this.recommendation += ((7-dateCount)*recommendation);
            this.todayRecommendation += recommendation;
            this.peopleCount += 1;
        }


    }


