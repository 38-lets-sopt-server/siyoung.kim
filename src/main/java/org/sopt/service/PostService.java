package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.global.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        // 1. 유효성 검증
        if (request.title() == null || request.title().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if (request.content() == null || request.content().isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }
        // 2. Post 도메인 객체 생성
        String createdAt = java.time.LocalDateTime.now().toString();
        Post post = new Post(
                postRepository.generateId(),
                request.title(),
                request.content(),
                request.author(),
                createdAt
        );
        // 3. 저장
        postRepository.save(post);
        // 4. 응답 DTO 조립해서 반환
        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
    }

    // GET /posts
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            postResponses.add(PostResponse.from(post));
        }

        return postResponses;
    }

    // GET posts/{id}
    public PostResponse getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        post.orElseThrow(() -> new PostNotFoundException());
        return PostResponse.from(post.get());
    }

    // PUT posts/{id}
    public PostResponse updatePost(Long id, UpdatePostRequest request) {
        Optional<Post> post = postRepository.findById(id);
        post.orElseThrow(() -> new PostNotFoundException());

        post.get().update(request.title(), request.content());

        return PostResponse.from(post.get());
    }

    // DELETE posts/{id}
    public void deletePost(Long id) {
        boolean isDeleted = postRepository.deleteById(id);

        if(!isDeleted) {
            throw new PostNotFoundException();
        }
    }
}