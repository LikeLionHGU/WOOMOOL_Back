package com.project.woomool.config;

import com.project.woomool.customAuthenticationFailureHandler.CustomAuthenticationFailureHandler;
import com.project.woomool.customAuthenticationFailureHandler.RestAuthenticationEntryPoint;
import com.project.woomool.customSuccessHandler.CustomSuccessHandler;
import com.project.woomool.jwt.JWTFilter;
import com.project.woomool.jwt.JWTUtil;
import com.project.woomool.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomSuccessHandler customSuccessHandler;
  private final JWTUtil jwtUtil;
  private final CustomAuthenticationFailureHandler customFailureHandler;
  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;



    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil, CustomAuthenticationFailureHandler customFailureHandler, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.customFailureHandler = customFailureHandler;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable);

    http.formLogin(AbstractHttpConfigurer::disable);

    http.httpBasic(AbstractHttpConfigurer::disable);


      // JWTFilter 추가
    http.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

    // oauth2
    http.oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuth2UserService))
            .successHandler(customSuccessHandler)
            .failureHandler(customFailureHandler)
    );

//     //경로별 인가 작업
//    http.authorizeHttpRequests(auth -> auth
//            .requestMatchers("/", "/post/main", "/posts/filtered", "/posts/{postId:\\d+}","/profile/{userId:\\d+}","/error").permitAll()
//            .requestMatchers("/admin/**").hasRole("ADMIN")
//            .anyRequest().authenticated());


    // 세션 설정 : STATELESS
    http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));




      return http.build();
  }
}
