package org.sopt.post.service;

import lombok.RequiredArgsConstructor;
import org.sopt.post.entity.BoardType;
import org.sopt.post.entity.Post;
import org.sopt.user.entity.User;
import org.sopt.post.dto.request.CreatePostRequest;
import org.sopt.post.dto.request.UpdatePostRequest;
import org.sopt.post.dto.response.CreatePostResponse;
import org.sopt.post.dto.response.PostResponse;
import org.sopt.global.common.code.ErrorCode;
import org.sopt.global.exception.BaseException;
import org.sopt.global.exception.PostNotFoundException;
import org.sopt.post.repository.PostRepository;
import org.sopt.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Transactional
    public CreatePostResponse createPost(Long userId, CreatePostRequest request) {

        // board 를 지정하지 않았을 때
        if(request.boardType() == null || request.boardType().isBlank()) {
            throw new BaseException(ErrorCode.POST_INVALID_BOARD);
        }

        // 존재하지 않는 board를 보냈을 때 BoardType 의 from 에서 에러 터지게 만들기
        BoardType boardType = BoardType.from(request.boardType());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Post post = new Post(
                request.title(),
                request.content(),
                boardType,
                user
        );
        postRepository.save(post);  // 영속성 컨텍스트에 올라감 → 커밋 시 INSERT
        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
    }

    @Transactional(readOnly = true)  // 조회 전용 → 더티 체킹 안 함 → 성능 최적화
    public List<PostResponse> getAllPosts(int page, int size, String boardName) {
        if(page < 0 || size <= 0) {
            throw new BaseException(ErrorCode.POST_INVALID_PAGINATION);
        }

        // JPA로 바꾸면서 Pageable 사용할 수 있게 됨! (id 내림차순으로 정렬해서 return)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Post> postPage;

        // boardName 은 안들어올 수도 있으니까 이렇게 체크해주기
        if(boardName == null || boardName.isBlank()) {
            postPage = postRepository.findAll(pageable);
        } else {
            BoardType boardType = BoardType.from(boardName);
            postPage = postRepository.findAllByBoardType(pageable, boardType);
        }

        // Page 의 map 함수를 통해서 List 로 변환
        return postPage.getContent().stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findIdWithUser(id)
                .orElseThrow(() -> new PostNotFoundException());
        return PostResponse.from(post);
    }

    @Transactional  // 변경 → 더티 체킹으로 save() 없이 자동 UPDATE, 현재는 없지만 권한 체크 구현 필요
    public PostResponse updatePost(Long userId, Long postId, UpdatePostRequest request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        // 만약 작성자가 아니라면 403 권한없음 에러 보내기
        if(!post.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        post.update(request.title(), request.content());

        // save() 호출 없어도 트랜잭션 커밋 시 UPDATE 쿼리 자동 실행
        return PostResponse.from(post);
    }

    // DELETE posts/{id}
    // 현재는 없지만 권한 체크 구현 필요
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        // 만약 작성자가 아니라면 403 권한없음 에러 보내기
        if(!post.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        postRepository.delete(post);
    }
}