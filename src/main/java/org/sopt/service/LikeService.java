package org.sopt.service;

import org.sopt.domain.Like;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BaseException;
import org.sopt.global.exception.PostNotFoundException;
import org.sopt.repository.LikeRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository,
                       PostRepository postRepository,
                       UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 좋아요 추가
    @Transactional
    public void likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        // 만약 같은 좋아요가 존재한다면 exception 던짐
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new BaseException(ErrorCode.LIKE_DUPLICATED);
        }

        likeRepository.save(new Like(user, post));
    }

    // 좋아요 취소
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        // 좋아요를 누른 적이 없다면 exception 던짐
        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new BaseException(ErrorCode.LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }
}
