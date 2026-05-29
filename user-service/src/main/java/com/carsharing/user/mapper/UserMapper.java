package com.carsharing.user.mapper;

import com.carsharing.shared.dto.UserResponse;
import com.carsharing.user.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.id);
        response.setFirstName(user.firstName);
        response.setLastName(user.lastName);
        response.setEmail(user.email);
        response.setRole(user.role.name());
        return response;
    }
}
