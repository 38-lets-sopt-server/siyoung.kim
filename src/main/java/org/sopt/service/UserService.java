package org.sopt.service;

import org.sopt.domain.User;
import org.sopt.dto.request.CreateUserRequest;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(CreateUserRequest createUser) {
        userRepository.save(new User(createUser.nickname(), createUser.email()));
    }
}
