package com.JPATest.JPATest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentDto {
    private Long postId;
    private String postTitle;
    private Long commentId;
    private String commentContent;
}