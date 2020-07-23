package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.comment.request.CommentRequest;
import com.skillbox.socialnetwork.main.dto.comment.response.CommentDto;
import com.skillbox.socialnetwork.main.dto.comment.response.CommentResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.dto.universal.Response;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.model.PostComment;
import com.skillbox.socialnetwork.main.model.enumerated.NotificationCode;
import com.skillbox.socialnetwork.main.repository.CommentRepository;
import com.skillbox.socialnetwork.main.repository.PostRepository;
import com.skillbox.socialnetwork.main.service.CommentService;
import com.skillbox.socialnetwork.main.service.NotificationService;
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
    private final NotificationService notificationService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, PersonService personService, NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.personService = personService;
        this.notificationService = notificationService;
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
        Person commentAuthor = personService.findById(authorId);
        Post post = postRepository.findPostById(postId);
        comment.setPost(post);
        comment.setTime(new Date());
        comment.setCommentText(request.getText());
        comment.setParentComment(commentRepository.findPostCommentById(request.getParentId()));
        comment.setAuthor(commentAuthor);
        comment.setIsBlocked(false);
        commentRepository.save(comment);

        //добавление уведомления о комментарии

        boolean isParentComment = comment.getParentComment() == null;   //проверка является ли комментарий ответом на комментарий или комментарием к посту
        boolean sameAuthor = commentAuthor.equals(post.getAuthor()) && isParentComment;

        if (!sameAuthor) {
            notificationService.addNotification(commentAuthor,
                    isParentComment ? post.getAuthor() : comment.getParentComment().getAuthor(),
                    isParentComment ? NotificationCode.POST_COMMENT : NotificationCode.COMMENT_COMMENT,
                    comment.getCommentText());
            log.info("SENT COMMENT notification to " + (isParentComment ? post.getAuthor() : comment.getParentComment().getAuthor()).getFirstName() + " " + (isParentComment ? post.getAuthor() : comment.getParentComment().getAuthor()).getLastName());
        }
        log.info("New comment added from user with id = {}", authorId);
        return CommentResponseFactory.getCommentDto(comment, CommentResponseFactory.getCommentList(comment.getChildComments(), comment, commentAuthor), commentAuthor);
    }

    @Override
    public Dto updateComment(int commentId, CommentRequest request) {
        PostComment comment = commentRepository.findPostCommentById(commentId);
        comment.setCommentText(request.getText());
        comment.setParentComment(commentRepository.findPostCommentById(request.getParentId()));
        commentRepository.save(comment);
        log.info("Comment with id {} successfully updated", commentId);
        return CommentResponseFactory.getCommentDto(comment, CommentResponseFactory.getCommentList(comment.getChildComments(), comment, comment.getAuthor()), comment.getAuthor());
    }

    @Override
    public Dto deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
        CommentDto dto = new CommentDto();
        dto.setId(commentId);
        log.info("Comment with id {} is deleted", commentId);
        return dto;
    }

    @Override
    public Response getComments(int postId, Person person) {
        Post postById = postRepository.findPostById(postId);
        return CommentResponseFactory.getComments(postById.getComments(), 10, 10, person);
    }
}
