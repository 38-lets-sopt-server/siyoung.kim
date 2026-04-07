package org.sopt.controller;

import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.response.ApiResponse;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.PostNotFoundException;
import org.sopt.service.PostService;

import java.util.List;

public class PostController {
    private final PostService postService = new PostService();

    // POST /posts
    public ApiResponse<CreatePostResponse> createPost(CreatePostRequest request) {
        try {
            CreatePostResponse response = postService.createPost(request);
            return ApiResponse.success(response.message, response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    // GET /posts 📝 과제
    public ApiResponse<List<PostResponse>> getAllPosts() {
        try {
            return ApiResponse.success("게시글 전체 조회 성공", postService.getAllPosts());
        } catch (RuntimeException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    // GET /posts/{id} 📝 과제
    public ApiResponse<PostResponse> getPost(Long id) {
        try {
            PostResponse response = postService.getPost(id);
            return ApiResponse.success("게시글 단건 조회 성공", response);
        } catch (PostNotFoundException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    // PUT /posts/{id} 📝 과제
    public ApiResponse<Void> updatePost(Long id, String newTitle, String newContent) {
        try {
            postService.updatePost(id, newTitle, newContent);
            return ApiResponse.success("게시글 수정 완료", null);
        } catch (PostNotFoundException | IllegalArgumentException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }

    // DELETE /posts/{id} 📝 과제
    public ApiResponse<Void> deletePost(Long id) {
        // TODO: postService.deletePost() 호출, 예외 발생 시 에러 메시지 출력
        try {
            postService.deletePost(id);
            return ApiResponse.success("게시글 삭제 성공", null);
        } catch (PostNotFoundException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }
}
