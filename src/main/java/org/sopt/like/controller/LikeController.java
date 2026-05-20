package org.sopt.like.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sopt.global.common.code.SuccessCode;
import org.sopt.global.common.response.BaseResponse;
import org.sopt.like.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    // 좋아요 추가
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> likePost(
            Authentication authentication,

            @PathVariable Long postId
    ) {
        Long userId = Long.parseLong(authentication.getName());

        likeService.likePost(postId, userId);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc);
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> cancelLike(
            Authentication authentication,

            @PathVariable Long postId
    ) {
        Long userId = Long.parseLong(authentication.getName());

        likeService.cancelLike(postId, userId);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc);
    }
}
