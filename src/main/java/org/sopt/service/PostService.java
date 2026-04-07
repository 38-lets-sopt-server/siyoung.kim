package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;

import java.util.List;

public class PostService {
    private final PostRepository postRepository = new PostRepository();

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        if (request.title == null || request.title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if (request.content == null || request.content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }
        String createdAt = java.time.LocalDateTime.now().toString();
        Post post = new Post(postRepository.generateId(), request.title, request.content, request.author, createdAt);
        postRepository.save(post);
        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
    }

    // READ - 전체 📝 과제
    public List<PostResponse> getAllPosts() {
        // TODO
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(p -> new PostResponse(p))
                .toList();
    }

    // READ - 단건 📝 과제
    public PostResponse getPost(Long id) {
        // TODO
        Post p = postRepository.findById(id);

        if(p == null) {
            throw new PostNotFoundException();
        }

        return new PostResponse(p);
    }

    // UPDATE 📝 과제
    public void updatePost(Long id, String newTitle, String newContent) {
        // TODO
        if(postRepository.findById(id) == null) {
            throw new PostNotFoundException();
        }

        if(newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다!");
        }
        if(newTitle.length() > 50) {
            throw new IllegalArgumentException("제목은 50자 이하여야합니다.");
        }
        if(newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다!");
        }

        Post p = postRepository.findById(id);
        p.update(newTitle, newContent);
        postRepository.save(p);
    }

    // DELETE 📝 과제
    public void deletePost(Long id) {
        // TODO
        boolean isDeleted = postRepository.deleteById(id);

        if(!isDeleted) {
            throw new PostNotFoundException();
        }
    }
}