package org.sopt.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.user.entity.User;
import org.sopt.user.dto.request.CreateUserRequest;
import org.sopt.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(CreateUserRequest createUser) {
        // encoder 로 암호화된 비밀번호로 db에 저장하도록 변경
        String encodedPassword = passwordEncoder.encode(createUser.password());

        userRepository.save(new User(
                createUser.nickname(),
                createUser.email(),
                encodedPassword
        ));
    }
}
