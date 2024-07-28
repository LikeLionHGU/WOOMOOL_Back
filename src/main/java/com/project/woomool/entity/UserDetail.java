package com.project.woomool.entity;

import com.project.woomool.controller.request.UserDetailRequest;
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
    private float recommendation;
    private float todayTotal;
    private int hasDrankLevel;
    private boolean hasDrankToday;
    private boolean warnDrankToday;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="User_Id" )
    private User user;

    public void updateTotal(float amount) {
        this.todayTotal += amount;
    }

    public void addDrankLevel(){this.hasDrankLevel++;}

    public static UserDetail of(UserDetailRequest request, float bmi, User user) {
        return UserDetail.builder()
            .weight(request.getWeight())
            .height(request.getHeight())
            .bmi(bmi)
            .recommendation(((request.getHeight()+ request.getWeight())/100)*1000)
            .hasDrankToday(false)
            .warnDrankToday(false)
            .hasDrankLevel(0)
            .todayTotal(0)
            .user(user)
            .build();
    }

    public void update(UserDetailRequest request, float bmi, float todayT){
        this.weight = request.getWeight();
        this.height=request.getHeight();
        this.bmi=bmi;
        this.recommendation=((request.getHeight()+ request.getWeight())/100)*1000;
        if((this.recommendation*0.8)<=todayT&&(this.recommendation+350)>=todayT) {
            this.hasDrankToday = true;
        }else if((this.recommendation+350)<todayT){
            this.hasDrankToday = false;
            this.warnDrankToday = true;
        }
    }
}
