package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.comment.request.CommentRequest;
import com.skillbox.socialnetwork.main.dto.comment.response.CommentDto;
import com.skillbox.socialnetwork.main.dto.comment.response.CommentResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.model.PostComment;
import com.skillbox.socialnetwork.main.repository.CommentRepository;
import com.skillbox.socialnetwork.main.repository.PostRepository;
import com.skillbox.socialnetwork.main.service.CommentService;
import com.skillbox.socialnetwork.main.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PersonService personService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, PersonService personService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.personService = personService;
    }

    @Override
    public PostComment findById(Integer id) {
        return commentRepository.findPostCommentById(id);
    }

    @Override
    public PostComment save(PostComment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Dto addComment(int postId, CommentRequest request, int authorId) {
        PostComment comment = new PostComment();
        comment.setPost(postRepository.findPostById(postId));
        comment.setTime(new Date());
        comment.setCommentText(request.getText());
        comment.setParentComment(commentRepository.findPostCommentById(request.getParentId()));
        comment.setAuthor(personService.findById(authorId));
        comment.setIsBlocked(false);
        commentRepository.save(comment);
        log.info("New comment added from user with id = {}", authorId);
        return CommentResponseFactory.getCommentDto(comment, CommentResponseFactory.getCommentList(comment.getChildComments(), comment));
    }

    @Override
    public Dto updateComment(int commentId, CommentRequest request) {
        PostComment comment = commentRepository.findPostCommentById(commentId);
        comment.setCommentText(request.getText());
        comment.setParentComment(commentRepository.findPostCommentById(request.getParentId()));
        commentRepository.save(comment);
        log.info("Comment with id {} successfully updated", commentId);
        return CommentResponseFactory.getCommentDto(comment, CommentResponseFactory.getCommentList(comment.getChildComments(), comment));
    }

    @Override
    public Dto deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
        CommentDto dto = new CommentDto();
        dto.setId(commentId);
        log.info("Comment with id {} is deleted", commentId);
        return dto;
    }
}
