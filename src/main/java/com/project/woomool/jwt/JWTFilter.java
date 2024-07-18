package com.project.woomool.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.woomool.controller.response.exception.ExceptionResponse;
import com.project.woomool.dto.CustomOAuth2UserDTO;
import com.project.woomool.dto.UserDTO;
import com.project.woomool.exception.DoNotLoginException;
import com.project.woomool.exception.ExpiredTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorization = null;
            Cookie[] cookies = request.getCookies();

            String requestUri = request.getRequestURI();

            if(requestUri.matches("/error")){
                filterChain.doFilter(request, response);
                return;
            }

            if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

                filterChain.doFilter(request, response);
                return;
            }
            if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

                filterChain.doFilter(request, response);
                return;
            }

            if (requestUri.matches("^\\/login(?:\\/.*)?$") || requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
                setErrorResponse(response, "올바르지 않은 접근입니다.", HttpStatus.BAD_REQUEST);
                throw new DoNotLoginException();

            }


            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("Authorization")) {
                        authorization = cookie.getValue();
                    }
                }
            } else {
                System.out.println("쿠기가 없습니다.");
            }

            if (authorization == null) {
                filterChain.doFilter(request, response);

                return;
            }


            String token = authorization;

            if (jwtUtil.isExpired(token)) {


                filterChain.doFilter(request, response);

                throw new ExpiredTokenException();

            }

            String username = jwtUtil.getUsername(token);
            String email = jwtUtil.getEmail(token);
            String role = jwtUtil.getRole(token);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setRole(role);
            userDTO.setEmail(email);

            CustomOAuth2UserDTO customOAuth2User = new CustomOAuth2UserDTO(userDTO);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        }catch (ExpiredTokenException e){
            setErrorResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
        }catch (DoNotLoginException e){
            setErrorResponse(response, e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    public static void setErrorResponse(
            HttpServletResponse response, String message, HttpStatus httpStatus) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ExceptionResponse exceptionResponse =
                ExceptionResponse.builder().error(httpStatus.getReasonPhrase()).message(message).build();
        try {
            response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
