package org.sopt.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.dto.request.CreateUserRequest;
import org.sopt.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Post 요청 시 사용자를 위해서 임시로 만든 컨트롤러, 서비스, 레포지토리! 추후 develop 필요
@Tag(name = "User", description = " 사용자 관련 API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void createUser(@Valid @RequestBody CreateUserRequest createUser) {
        userService.createUser(createUser);
    }
}
