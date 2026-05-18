package org.sopt.post.controller.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.sopt.global.common.response.ErrorResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class PostApiResponses {
    private PostApiResponses() {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "게시글 생성",
            description = "게시글을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "1. 사용자가 존재하지 않을 때\n" +
                            "2. 존재하지 않는 게시판 종류일 때",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "1. 게시판을 지정 안했을 때\n" +
                            "2. 유효성 검증 실패(제목 글자 수 초과 or 내용 누락",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public @interface CreatePost {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "게시글 전체 조회",
            description = "boardType 지정 시 해당 게시판의 게시글을 페이지네이션을 적용하여 전체 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 전체 조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "1. 사용자가 존재하지 않을 때\n" +
                            "2. 존재하지 않는 게시판 종류일 때",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 — page < 0 이거나 size <= 0 일 때\n",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public @interface GetAllPosts {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "게시글 단건 조회",
            description = "게시글 ID로 특정 게시글을 조회합니다. 삭제된 게시글은 조회되지 않아요."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 단건 조회 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 id 의 게시글이 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public @interface GetPost {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "게시글 수정",
            description = "게시글 ID로 특정 게시글을 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효성 검증 실패(제목 글자 수 초과 or 내용 누락",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 id 의 게시글이 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public @interface UpdatePost {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "게시글 삭제",
            description = "게시글 ID로 특정 게시글을 삭제합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 id 의 게시글이 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public @interface DeletePost {}
}
