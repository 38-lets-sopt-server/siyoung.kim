package org.sopt.dto.response;

import org.sopt.domain.User;

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
