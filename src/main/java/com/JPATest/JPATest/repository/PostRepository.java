package com.JPATest.JPATest.repository;

import com.JPATest.JPATest.dto.PostCommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.JPATest.JPATest.domain.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 1. 기본 방식 → N+1 발생
    @Query("SELECT p FROM Post p")
    List<Post> findAllBasic();

    // 2. Fetch Join 사용 → 댓글까지 한 번에 조회
    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.comments")
    List<Post> findAllWithFetchJoin();

    // 3. DTO Projection → 필요한 데이터만 추출
    @Query("SELECT new com.JPATest.JPATest.dto.PostCommentDto(p.id, p.title, c.id, c.content) " +
            "FROM Post p JOIN p.comments c")
    List<PostCommentDto> findAllAsDto();




    // 1. 기본 방식 → N+1 발생
    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.id = :id ")
    List<Post> findBasicById(@Param("id") Long id);

    // 2. Fetch Join 사용 → 댓글까지 한 번에 조회
    @Query("SELECT DISTINCT p " +
            "FROM Post p " +
            "JOIN FETCH p.comments " +
            "WHERE p.id = :id ")
    List<Post> findWithFetchJoinById(@Param("id") Long id);

    // 3. DTO Projection → 필요한 데이터만 추출
    @Query("SELECT new com.JPATest.JPATest.dto.PostCommentDto(p.id, p.title, c.id, c.content) " +
            "FROM Post p JOIN p.comments c " +
            "WHERE p.id = :id ")
    List<PostCommentDto> findAsDtoById(@Param("id") Long id);
}
