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
    private float recommendation; // 오늘 마셔야 하는 양
    private float todayTotal; //오늘 마신 양
    private int hasDrankLevel; //0
    private boolean hasDrankToday; //완
    private boolean warnDrankToday; //0
    private int cup;
    private int week; //유저 시작한지 몇주차인지
    private int weekDate; // 일주일중 몇일 지났는지
    private boolean attendance;

    private float weekBeforeWater; //지난 일주일 마신 양

    private float weekBeforeRecommendation; //지난 일주일 추천량

    private float weekRecommendation; // 처음 들어올때는 *7, 그 다음날부터는 weekBeforeRecommendation + recommendation*7

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name ="User_Id" )
    private User user;

    public void updateTotal(float amount) {
        this.todayTotal += amount;
    }

    public void addDrankLevel(){this.hasDrankLevel++;}
    public void addWeekDate(){this.weekDate++;}
    public void addWeek(){this.week++;}
    public void addWeekBeforeWater(float amount){this.weekBeforeWater+=amount;}

    public void addWeekBeforeRecommendation(float amount){this.weekBeforeRecommendation+=amount;}

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
            .cup(0)
            .week(1)
            .weekDate(0)
            .attendance(false)
            .weekBeforeWater(0)
            .weekBeforeRecommendation(0)
            .weekRecommendation((((request.getHeight()+ request.getWeight())/100)*1000)*7)
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

    public void changeCup(int cup){
        this.cup = cup;
    }
}
