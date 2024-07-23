package com.project.woomool.customSuccessHandler;


import com.project.woomool.dto.CustomOAuth2UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.project.woomool.jwt.JWTUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2UserDTO customUserDetails = (CustomOAuth2UserDTO) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        System.out.println("=======");
        System.out.println(username);


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        System.out.println("=======");
        String email = customUserDetails.getEmail();

        System.out.println(email);
        System.out.println("=======");
        System.out.println(role);

        String token = jwtUtil.createJwt(username, email,role, 1000 * 60 * 60 * 24 * 30L);

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        response.setStatus(HttpServletResponse.SC_OK);
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String successMessage = URLEncoder.encode("Authentication successful", StandardCharsets.UTF_8);

        String redirectUrl = String.format("http://localhost:3000/logincb?status=success&jwt=%s&message=%s", encodedToken, successMessage);

        // 리다이렉트


        // Authorization 헤더에 토큰 설정
        response.setHeader("Authorization", "Bearer " + token);

        // 클라이언트에게 성공 메시지와 함께 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\":true,\"message\":\"Authentication successful\"}");

        response.sendRedirect(redirectUrl);

    }

}
