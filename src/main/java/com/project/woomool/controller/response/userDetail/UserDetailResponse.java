package com.project.woomool.controller.response.userDetail;

import com.project.woomool.dto.UserDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserDetailResponse {

    private float weight;
    private float height;
    private float bmi;
    private float recommendation;

    public UserDetailResponse (UserDetailDto dto) {
            this.weight = dto.getWeight();
            this.height = dto.getHeight();
            this.bmi = dto.getBmi();
            this.recommendation = dto.getRecommendation();
    }
}
