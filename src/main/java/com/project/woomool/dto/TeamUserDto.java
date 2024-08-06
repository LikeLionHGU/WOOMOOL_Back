package com.project.woomool.dto;

import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamUserDto {

    private Long userId;
    private String username;
    private String nickName;
    private float waterAmount;
    private float recommendation;



    public static TeamUserDto of(User user, float water, float recommendation) {
        return TeamUserDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickName(user.getNickName())
                .waterAmount(water)
                .recommendation(recommendation)
                .build();
    }

}