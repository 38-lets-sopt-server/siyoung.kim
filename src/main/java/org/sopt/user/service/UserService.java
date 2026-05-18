package org.sopt.user.service;

import org.sopt.user.entity.User;
import org.sopt.user.dto.request.CreateUserRequest;
import org.sopt.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(CreateUserRequest createUser) {
        userRepository.save(new User(createUser.nickname(), createUser.email(), createUser.password()));
    }
}
