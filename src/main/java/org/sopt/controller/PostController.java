package org.sopt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.global.code.SuccessCode;
import org.sopt.global.response.BaseResponse;
import org.sopt.service.PostService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // --------------------------------------
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
                    description = "1. 게시판을 지정 안했을 때\n" +
                            "2. 유효성 검증 실패(제목 글자 수 초과 or 내용 누락")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<CreatePostResponse>> createPost(
            @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(request);
        SuccessCode sc = SuccessCode.SUCCESS_CREATED;

        return BaseResponse.success(sc, response);
    }

    // --------------------------------------
    @Operation(
            summary = "게시글 전체 조회",
            description = "boardType 지정 시 해당 게시판의 게시글을 페이지네이션을 적용하여 전체 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 전체 조회 성공"),
            @ApiResponse(responseCode = "404",
                    description = "1. 사용자가 존재하지 않을 때\n" +
                            "2. 존재하지 않는 게시판 종류일 때"),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청 — page < 0 이거나 size <= 0 일 때\n"
            )
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<PostResponse>>> getAllPosts(
            @Parameter(description = "조회할 페이지, default = 0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "가져올 Post의 사이즈, default = 10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "조회할 게시판 타입, 필터링이 필요할 때만 입력", example = "free")
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

    // --------------------------------------
    @Operation(
            summary = "게시글 단건 조회",
            description = "게시글 ID로 특정 게시글을 조회합니다. 삭제된 게시글은 조회되지 않아요."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 단건 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 id 의 게시글이 존재하지 않을 때")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(
            @Parameter(description = "조회할 게시글 id", example = "1", required = true)
            @PathVariable Long id
    ) {
        //TODO
        PostResponse response = postService.getPost(id);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc, response);
    }

    // --------------------------------------
    @Operation(
            summary = "게시글 수정",
            description = "게시글 ID로 특정 게시글을 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패(제목 글자 수 초과 or 내용 누락"),
            @ApiResponse(responseCode = "404", description = "해당 id 의 게시글이 존재하지 않을 때")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PostResponse>> updatePost(
            @Parameter(description = "수정할 게시글 id", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody UpdatePostRequest request
    ) {
        //TODO
        PostResponse response = postService.updatePost(id, request);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return BaseResponse.success(sc, response);
    }

    // --------------------------------------
    @Operation(
            summary = "게시글 삭제",
            description = "게시글 ID로 특정 게시글을 삭제합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 id 의 게시글이 존재하지 않을 때")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deletePost(
            @Parameter(description = "삭제할 게시글 id", example = "1", required = true)
            @PathVariable Long id
    ) {
        //TODO
        postService.deletePost(id);
        SuccessCode sc = SuccessCode.SUCCESS_OK;
        return BaseResponse.success(sc);

    }


}
