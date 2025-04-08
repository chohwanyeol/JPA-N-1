package com.JPATest.JPATest.init;

import com.JPATest.JPATest.domain.Comment;
import com.JPATest.JPATest.domain.Post;
import com.JPATest.JPATest.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final PostRepository postRepository;

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 10; i++) {
            Post post = Post.builder()
                    .title("게시글 " + i)
                    .build();

            for (int j = 1; j <= 5; j++) {
                Comment comment = Comment.builder()
                        .content("댓글 " + j + " (게시글 " + i + ")")
                        .post(post)
                        .build();

                post.getComments().add(comment);
            }

            postRepository.save(post);
        }
    }
}
