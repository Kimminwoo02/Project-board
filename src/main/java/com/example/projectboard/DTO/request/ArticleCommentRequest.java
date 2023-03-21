package com.example.projectboard.DTO.request;


import com.example.projectboard.DTO.ArticleCommentDto;
import com.example.projectboard.DTO.UserAccountDto;


public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                content
        );
    }

}

