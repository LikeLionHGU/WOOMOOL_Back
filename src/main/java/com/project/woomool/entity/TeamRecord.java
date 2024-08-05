package com.project.woomool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "User_Id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Team_Id")
    private Team team;

    private LocalDate date;
    private float amount;

    public static TeamRecord of(UserRecord userRecord, Team team) {
        return TeamRecord.builder()
                .user(userRecord.getUser())
                .team(team)
                .date(LocalDate.now())
                .amount(userRecord.getAmount())
                .build();
    }
}
