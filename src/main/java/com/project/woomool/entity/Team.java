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
    private float recommendation;
    private float groupTotal;

    public static Team of(TeamDto dto) {
        return Team.builder()
            .name(dto.getName())
            .code(dto.getCode())
            .recommendation(dto.getRecommendation())
            .groupTotal(dto.getGroupTotal())
            .build();
    }

}
