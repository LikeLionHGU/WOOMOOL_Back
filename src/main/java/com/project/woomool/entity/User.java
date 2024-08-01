package com.project.woomool.entity;

import com.project.woomool.controller.request.UserNickNameRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String role;
    private String name;
    private String nickName;
    private int cup;

    public void addNickName(UserNickNameRequest request){
        this.nickName = request.getNickName();
    }

}
