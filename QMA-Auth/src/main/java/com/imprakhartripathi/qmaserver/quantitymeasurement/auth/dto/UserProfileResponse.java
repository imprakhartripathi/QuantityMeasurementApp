package com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.UserEntity;

import java.util.List;

public class UserProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private String provider;
    private List<Object> history;

    public static UserProfileResponse fromUser(UserEntity userEntity) {
        UserProfileResponse response = new UserProfileResponse();
        response.id = userEntity.getId();
        response.name = userEntity.getName();
        response.email = userEntity.getEmail();
        response.picture = userEntity.getPicture();
        response.provider = userEntity.getProvider().name();
        response.history = List.of();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public String getProvider() {
        return provider;
    }

    public List<Object> getHistory() {
        return history;
    }
}
