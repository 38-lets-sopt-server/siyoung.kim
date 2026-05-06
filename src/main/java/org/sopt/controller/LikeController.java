package org.sopt.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.dto.request.CreateLikePostRequest;
import org.sopt.global.code.SuccessCode;
import org.sopt.global.response.BaseResponse;
import org.sopt.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 좋아요 추가
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> likePost(
            @PathVariable Long postId,
            @Valid @RequestBody CreateLikePostRequest request
    ) {
        likeService.likePost(postId, request.userId());
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc);
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> cancelPost(
            @PathVariable Long postId,
            @Valid @RequestBody CreateLikePostRequest request
    ) {
        likeService.unlikePost(postId, request.userId());
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc);
    }
}
