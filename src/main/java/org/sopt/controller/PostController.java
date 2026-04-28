package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.domain.BoardType;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.global.code.SuccessCode;
import org.sopt.global.response.BaseResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "게시글 생성",           // Swagger UI에서 API 이름으로 보여요
            description = "게시글을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "404",
                    description = "1. 사용자가 존재하지 않을 때\n" +
                            "2. 존재하지 않는 게시판 종류일 때"),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청 — 게시판을 지정 안했을 때")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<CreatePostResponse>> createPost(
            @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(request);
        SuccessCode sc = SuccessCode.SUCCESS_CREATED;

        return BaseResponse.success(sc, response);
    }

    @Operation(
            summary = "게시글 전체 조회",
            description = "boardType 지정 시 해당 게시판의 게시글을 페이지네이션을 적용하여 전체 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "404",
                    description = "1. 사용자가 존재하지 않을 때\n" +
                            "2. 존재하지 않는 게시판 종류일 때"),
            @ApiResponse(responseCode = "400",
                    description = "1. 잘못된 요청 — page < 0 이거나 size <= 0 일 때\n" +
                            "2. 존재하지 않는 게시판 종류일 때"
            )
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String boardType
    ) {
        //TODO
        List<PostResponse> response;
        if(boardType == null || boardType.isBlank()) {
            response = postService.getAllPosts(page, size);
        } else {
            response = postService.getAllPosts(page, size, boardType);
        }
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc, response);
    }

    @Operation(
            summary = "게시글 단건 조회",           // Swagger UI에서 API 이름으로 보여요
            description = "게시글 ID로 특정 게시글을 조회합니다. 삭제된 게시글은 조회되지 않아요."
    )
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(
            @PathVariable Long id
    ) {
        //TODO
        PostResponse response = postService.getPost(id);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc, response);
    }

    // PUT /posts/{id} 📝 과제
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @RequestBody UpdatePostRequest request
    ) {
        //TODO
        PostResponse response = postService.updatePost(id, request);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc, response);
    }

    // DELETE /posts/{id} 📝 과제
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deletePost(
            @PathVariable Long id
    ) {
        //TODO
        postService.deletePost(id);
        /* delete 시 body 없는 게 나을 거 같은데(204) 공통 응답 객체로 전부 통일 하고 싶어서
         status code 를 200으로 통일... 이 부분은 body 없이 바로 .noContent.build() 해도 될 거 같음 */
        SuccessCode sc = SuccessCode.SUCCESS_OK;
        return BaseResponse.success(sc);

    }


}
