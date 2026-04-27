package org.sopt.service;

import org.sopt.domain.BoardType;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BaseException;
import org.sopt.global.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.sopt.validator.PostValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,  UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // CREATE
//    public CreatePostResponse createPost(CreatePostRequest request) {
//        // 1. 유효성 검증(PostValidator 활용)
//        PostValidator.validateTitle(request.title());
//        PostValidator.validateContent(request.content());
//
//        // board 를 지정하지 않았을 때
//        if(request.boardType() == null || request.boardType().isBlank()) {
//            throw new BaseException(ErrorCode.POST_INVALID_BOARD);
//        }
//
//        // 존재하지 않는 board 를 보냈을 때는 BoardType의 from 에서 에러 터지게 만듦
//        BoardType boardType = BoardType.from(request.boardType());
//
//        // 2. Post 도메인 객체 생성
//        String createdAt = java.time.LocalDateTime.now().toString();
//        Post post = new Post(
//                postRepository.generateId(),
//                request.title(),
//                request.content(),
//                request.author(),
//                createdAt,
//                boardType
//        );
//        // 3. 저장
//        postRepository.save(post);
//        // 4. 응답 DTO 조립해서 반환
//        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
//    }

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Post post = new Post(request.title(), request.content(), user);
        postRepository.save(post);  // 영속성 컨텍스트에 올라감 → 커밋 시 INSERT
        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
    }

    // GET /posts <- 페이지네이션 적용하는 메소드로 변경!
//    public List<PostResponse> getAllPosts(int page, int size) {
//
//        if(page < 0 || size < 0) {
//            throw new BaseException(ErrorCode.POST_INVALID_PAGINATION);
//        }
//
//        List<Post> posts = postRepository.findAll();
//
//        int start = page * size;
//        // 끝 부분은 start + size랑 List<Post> 중 누가 더 작냐를 비교해야함
//        int end = Math.min(start + size, posts.size());
//
//        List<PostResponse> postResponses = new ArrayList<>();
//
//        posts = posts.subList(start, end);
//
//        for(Post post : posts) {
//            postResponses.add(PostResponse.from(post));
//        }
//
//        return postResponses;
//    }

    @Transactional(readOnly = true)  // 조회 전용 → 더티 체킹 안 함 → 성능 최적화
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    // GET posts/{id}
//    public PostResponse getPost(Long id) {
//        Optional<Post> post = postRepository.findById(id);
//        post.orElseThrow(() -> new PostNotFoundException());
//        return PostResponse.from(post.get());
//    }
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        return PostResponse.from(post);
    }

    public List<PostResponse> getAllPostByBoardName(BoardType boardType) {
        List<Post> posts = postRepository.findAllByBoardType(boardType);
        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            postResponses.add(PostResponse.from(post));
        }
        return postResponses;
    }

    // PUT posts/{id}
//    public PostResponse updatePost(Long id, UpdatePostRequest request) {
//        Optional<Post> post = postRepository.findById(id);
//        post.orElseThrow(() -> new PostNotFoundException());
//
//        PostValidator.validateTitle(request.title());
//        PostValidator.validateContent(request.content());
//
//        post.get().update(request.title(), request.content());
//
//        return PostResponse.from(post.get());
//    }

    @Transactional  // 변경 → 더티 체킹으로 save() 없이 자동 UPDATE
    public PostResponse updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        post.update(request.title(), request.content());
        // save() 호출 없어도 트랜잭션 커밋 시 UPDATE 쿼리 자동 실행
        return PostResponse.from(post);
    }

    // DELETE posts/{id}
    public void deletePost(Long id) {
//        boolean isDeleted = postRepository.deleteById(id);
//
//        if(!isDeleted) {
//            throw new PostNotFoundException();
//        }
    }
}