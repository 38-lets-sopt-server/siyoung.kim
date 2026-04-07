package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.sopt.validator.PostValidator;

import java.util.List;

public class PostService {
    private final PostRepository postRepository = new PostRepository();

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {

        //PostValidator 도입 -> 서비스 계층에서 유효성 검증 책임 분리
        PostValidator.validateTitle(request.title);
        PostValidator.validateContent(request.content);

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
        Post p = postRepository.findById(id);
        if(p == null) {
            throw new PostNotFoundException();
        }

        PostValidator.validateTitle(newTitle);
        PostValidator.validateContent(newContent);

        p.update(newTitle, newContent);
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