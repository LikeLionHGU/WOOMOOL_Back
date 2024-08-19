package com.project.woomool.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDetail extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "User_Id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Team_Id")
    private Team team;

    private float waterAmount; //어제까지 마신 물의 양  - 추가하는 것 완료, * 리셋 부분 미완료
    private float pastWaterRecommendation; //어제까지의 추천량의 합

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
