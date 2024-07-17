package com.project.woomool.controller.response.loginResponse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginResponse {

    private String username;
    private String email;
    private String role;
    private String id_Code;

    public LoginResponse(OAuth2User user) {
        this.username = user.getAttribute("name");
        this.email = user.getAttribute("email");
        this.role = user.getAttribute("role");
        this.id_Code = user.getAttribute("idCode");

     }
}
