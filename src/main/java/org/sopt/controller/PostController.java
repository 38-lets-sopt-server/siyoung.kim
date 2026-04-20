package org.sopt.controller;

import org.sopt.domain.BoardType;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.global.code.SuccessCode;
import org.sopt.global.response.ApiResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST /posts ✅ 같이 구현
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse response = postService.createPost(request);
        SuccessCode sc = SuccessCode.SUCCESS_CREATED;

        return ResponseEntity
                .status(sc.getStatus())
                .body(ApiResponse.success(sc, response));
    }


    // GET /posts 📝 과제
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        //TODO
        List<PostResponse> response = postService.getAllPosts();
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return ResponseEntity
                .status(sc.getStatus())
                .body(ApiResponse.success(sc, response));

    }

    // GET /posts/{id} 📝 과제
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(
            @PathVariable Long id
    ) {
        //TODO
        PostResponse response = postService.getPost(id);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return ResponseEntity
                .status(sc.getStatus())
                .body(ApiResponse.success(sc, response));
    }

    // GET /posts/{boardName} <- boardName은 free 이런식으로 소문자로 들어올거임
    @GetMapping("/board/{boardName}")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPostByBoardName(
            @PathVariable String boardName
    ) {
        BoardType boardType = BoardType.from(boardName);

        List<PostResponse> response = postService.getAllPostByBoardName(boardType);

        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return ResponseEntity
                .status(sc.getStatus())
                .body(ApiResponse.success(sc, response));
    }

    // PUT /posts/{id} 📝 과제
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @RequestBody UpdatePostRequest request
    ) {
        //TODO
        PostResponse response = postService.updatePost(id, request);
        SuccessCode sc = SuccessCode.SUCCESS_OK;

        return ResponseEntity
                .status(sc.getStatus())
                .body(ApiResponse.success(sc, response));
    }

    // DELETE /posts/{id} 📝 과제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id
    ) {
        //TODO
        postService.deletePost(id);
        /* delete 시 body 없는 게 나을 거 같은데(204) 공통 응답 객체로 전부 통일 하고 싶어서
         status code 를 200으로 통일... 이 부분은 body 없이 바로 .noContent.build() 해도 될 거 같음 */
        SuccessCode sc = SuccessCode.SUCCESS_OK;
        return ResponseEntity
                .status(sc.getStatus())
                .body(ApiResponse.success(sc, null));

    }


}
