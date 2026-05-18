package org.sopt.user.dto.response;

import org.sopt.user.entity.User;

public record UserResponse (
        Long id,
        String email,
        String password
){
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
