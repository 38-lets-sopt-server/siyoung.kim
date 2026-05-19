package org.sopt.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.user.dto.request.CreateUserRequest;
import org.sopt.global.common.code.SuccessCode;
import org.sopt.global.common.response.BaseResponse;
import org.sopt.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Post 요청 시 사용자를 위해서 임시로 만든 컨트롤러, 서비스, 레포지토리! 추후 develop 필요
@Tag(name = "User", description = " 사용자 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 사용자 생성 api
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createUser(@Valid @RequestBody CreateUserRequest createUser) {
        userService.createUser(createUser);
        SuccessCode sc = SuccessCode.SUCCESS_CREATED;

        return BaseResponse.success(sc);
    }
}
