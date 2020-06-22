package com.skillbox.socialnetwork.main.dto.comment.response;

import com.skillbox.socialnetwork.main.dto.person.response.PersonResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.PostComment;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CommentResponseFactory {
    public static CommentDto getCommentDto(PostComment comment, List<Dto> subList) {
        return new CommentDto(
                comment.getParentComment() == null ? 0 : comment.getParentComment().getId(),
                comment.getCommentText(),
                comment.getId(),
                comment.getPost().getId(),
                comment.getTime().getTime(),
                PersonResponseFactory.getPersonDto(comment.getAuthor()),
                comment.getIsBlocked(),
                subList
        );
    }

    public static BaseResponseList getComments(List<PostComment> commentList, int offset, int limit) {
        return ResponseFactory.getBaseResponseList(getCommentList(commentList, null), commentList.size(), offset, limit
        );
    }

    public static List<Dto> getCommentList(List<PostComment> commentList, PostComment parentComment) {
        List<Dto> list = new ArrayList<>();
        if (commentList == null){
            return list;
        }
        for (PostComment comment : commentList){
            if((parentComment == null && comment.getParentComment() == null) ||
                    (parentComment != null &&
                            comment.getParentComment().getId().equals(parentComment.getId()))) {
                list.add(CommentResponseFactory.getCommentDto(comment,
                        getCommentList(comment.getChildComments(), comment)));
            }
        }
        return list;
    }
}