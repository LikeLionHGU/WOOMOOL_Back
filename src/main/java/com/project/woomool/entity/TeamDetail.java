package com.project.woomool.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Team_Id")
    private Team team;

    private float waterAmount;
    private float pastWaterRecommendation;

    public static TeamDetail of(User user, Team team) {
        return TeamDetail.builder()
            .user(user)
            .team(team)
            .waterAmount(0)
            .pastWaterRecommendation(0)
            .build();
    }


    public void addWater(float amount) {
        this.waterAmount += amount;
    }

    public void addPastRecommendation(float amount) {
        this.pastWaterRecommendation += amount;
    }
}
