package org.sopt.repository;

import org.sopt.domain.BoardType;
import org.sopt.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Jpa 가 있기 때문에 Pageable, Page 사용 가능해짐
    @Query("select p from Post p join fetch p.user")
    Page<Post> findAll(Pageable pageable);

    @Query("select p from Post p join fetch p.user where p.boardType = :boardType")
    Page<Post> findAllByBoardType(Pageable pageable, BoardType boardType);
}
