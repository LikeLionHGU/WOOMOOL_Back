package com.project.woomool.dto;

import com.project.woomool.entity.User;
import com.project.woomool.entity.UserDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String role;
    private String username;
    private String name;
    private String email;
    private Long id;
    private String nickName;

    public static UserDTO of(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setNickName(user.getNickName());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
