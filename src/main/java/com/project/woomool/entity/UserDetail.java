package com.project.woomool.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float weight;
    private float height;
    private float bmi;
    private Long recommendation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="User_Id" )
    private User user;


}
