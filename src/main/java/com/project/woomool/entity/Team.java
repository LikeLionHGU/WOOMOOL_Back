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
    private float finalRecommendation; //최종 물 양
    private float recommendation;
    private float groupTotal;

    public static Team of(TeamDto dto) {
        return Team.builder()
            .name(dto.getName())
            .code(dto.getCode())
            .finalRecommendation(dto.getFinalRecommendation())
            .completeLevel(dto.getCompleteLevel())
            .dateCount(dto.getDateCount())
            .recommendation(dto.getRecommendation())
            .groupTotal(dto.getGroupTotal())
            .build();
    }

    public void updateTotal(float amount) {
        this.groupTotal += amount;
    }
    public void plusDateCount(){this.dateCount++;}

    public void plusCompleteLevel(){this.completeLevel++;}

    public void updateByJoin(float recommendation ) {
            this.recommendation += ((7-dateCount)*recommendation);
            this.finalRecommendation += (7*recommendation);
        }
    }


