package com.project.woomool.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Builder
public class CustomOAuth2UserDTO implements OAuth2User{


    private final UserDTO userDTO;

    public CustomOAuth2UserDTO(UserDTO userDTO) {

        this.userDTO = userDTO;
    }


    @Override
    public Map<String, Object> getAttributes() {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", userDTO.getEmail());
        attributes.put("name", userDTO.getName());
        attributes.put("username", userDTO.getUsername());
        attributes.put("role", userDTO.getRole());

        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return userDTO.getName();
    }

    public String getUsername() {

        return userDTO.getUsername();
    }

    public String getEmail() {

    return userDTO.getEmail();
  }

}
