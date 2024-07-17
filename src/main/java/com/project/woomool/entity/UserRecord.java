package com.project.woomool.entity;

import com.project.woomool.dto.UserRecordDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecord{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    private LocalDate date;
    private float amount;


    public static UserRecord of(UserRecordDto dto,User user){
            return UserRecord.builder()
                .user(user)
                .date(dto.getDate())
                .amount(dto.getAmount())
                .build();
    }
}
