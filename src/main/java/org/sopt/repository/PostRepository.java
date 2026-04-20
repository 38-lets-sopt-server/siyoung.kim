package org.sopt.repository;

import org.sopt.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);
    List<Post> findAll();
    Optional<Post> findById(Long id);
    boolean deleteById(Long id);
    Long generateId();
}
