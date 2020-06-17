package com.skillbox.socialnetwork.main.dto.comment.response;

import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.PostComment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentResponseFactory {
    public static CommentDto getCommentDto(PostComment comment) {
        return new CommentDto(
                comment.getParentComment() == null ? 0 : comment.getParentComment().getId(),
                comment.getCommentText(),
                comment.getId(),
                String.valueOf(comment.getId()), //TODO: Исправить эту заглушку
                comment.getTime().getTime(),
                comment.getAuthor().getId(),
                comment.getIsBlocked()
        );
    }

    public static BaseResponseList getComments(List<PostComment> commentList, int offset, int limit) {
        return ResponseFactory.getBaseResponseList(
                commentList.stream().map(CommentResponseFactory::getCommentDto)
                        .collect(Collectors.toList()), commentList.size(), offset, limit
        );
    }

    public static List<CommentDto> getCommentList(List<PostComment> commentList) {
        return commentList.size() == 0 ?
                new ArrayList<>() :
                commentList.stream().map(CommentResponseFactory::getCommentDto).collect(Collectors.toList());

    }
}