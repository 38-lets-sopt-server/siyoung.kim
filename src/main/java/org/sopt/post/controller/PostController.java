package org.sopt.post.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.post.controller.annotation.PostApiResponses;
import org.sopt.post.dto.request.CreatePostRequest;
import org.sopt.post.dto.request.UpdatePostRequest;
import org.sopt.post.dto.response.CreatePostResponse;
import org.sopt.post.dto.response.PostResponse;
import org.sopt.global.common.code.SuccessCode;
import org.sopt.global.common.response.BaseResponse;
import org.sopt.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // --------------------------------------
    @PostApiResponses.CreatePost
    @PostMapping
    public ResponseEntity<BaseResponse<CreatePostResponse>> createPost(
            Authentication authentication,
            @Valid @RequestBody CreatePostRequest request
    ) {
        // 이제 spring security 의 authentication 객체를 꺼내서 사용
        Long userId = Long.parseLong(authentication.getName());

        CreatePostResponse response = postService.createPost(userId, request);
        SuccessCode sc = SuccessCode.SUCCESS_CREATED;

        return BaseResponse.success(sc, response);
    }

    // --------------------------------------
    @PostApiResponses.GetAllPosts
    @GetMapping
    public ResponseEntity<BaseResponse<List<PostResponse>>> getAllPosts(
            @Parameter(description = "조회할 페이지, default = 0")
            @RequestParam(defaultValue = "0") int page, // RequestParam은 필터링 할 때 사용(URL 끝에 ? 붙여서 k-v쌍으로..)

            @Parameter(description = "가져올 Post의 사이즈, default = 10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "조회할 게시판 타입, 필터링이 필요할 때만 입력", example = "free")
            @RequestParam(required = false) String boardType
    ) {
        List<PostResponse> response = postService.getAllPosts(page, size, boardType);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc, response);
    }

    // --------------------------------------
    @PostApiResponses.GetPost
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(
            @Parameter(description = "조회할 게시글 id", example = "1", required = true)
            @PathVariable Long id // PathVariable은 URL 경로의 일부를 변수로 사용, 어떤 자원을 식별할 때 사용
    ) {
        PostResponse response = postService.getPost(id);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc, response);
    }

    // --------------------------------------
    @PostApiResponses.UpdatePost
    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponse>> updatePost(
            Authentication authentication,

            @Parameter(description = "수정할 게시글 id", example = "1", required = true)
            @PathVariable Long postId,

            @Valid @RequestBody UpdatePostRequest request
    ) {
        Long userId = Long.parseLong(authentication.getName());

        PostResponse response = postService.updatePost(userId, postId, request);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc, response);
    }

    // --------------------------------------
    @PostApiResponses.DeletePost
    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> deletePost(
            Authentication authentication,

            @Parameter(description = "삭제할 게시글 id", example = "1", required = true)
            @PathVariable Long postId
    ) {
        Long userId =  Long.parseLong(authentication.getName());

        postService.deletePost(userId, postId);
        SuccessCode sc = SuccessCode.SUCCESS_OK;
        return BaseResponse.success(sc);

    }


}
