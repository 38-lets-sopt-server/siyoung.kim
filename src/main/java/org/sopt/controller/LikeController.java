package org.sopt.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.global.code.SuccessCode;
import org.sopt.global.response.BaseResponse;
import org.sopt.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "좋아요 관련 API")
@RestController
@RequestMapping("/api/v1/posts/{postId}/like")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 좋아요 추가
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> likePost(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        likeService.likePost(postId, userId);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc);
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> deletePost(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        likeService.unlikePost(postId, userId);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc);
    }
}
