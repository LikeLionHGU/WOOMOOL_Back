package com.project.woomool.dto;

import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailDto {

    private float weight;
    private float height;
    private float bmi;
    private float recommendation;

    public static UserDetailDto of(UserDetail userDetail) {
        UserDetailDto dto = new UserDetailDto();
        dto.setWeight(userDetail.getWeight());
        dto.setHeight(userDetail.getHeight());
        dto.setBmi(userDetail.getBmi());
        dto.setRecommendation(userDetail.getRecommendation());
        return dto;
    }
}
