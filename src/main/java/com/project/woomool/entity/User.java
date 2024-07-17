package com.project.woomool.entity;

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
    private byte hasProfile;



    public User(String username, String email, String role , String idCode) {
        this.username = username;
        this.email = email;
        this.name = idCode;
        this.hasProfile=0;
    }

    public void doneProfile() {
            this.hasProfile = 1;
    }

    }

