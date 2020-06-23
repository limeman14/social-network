package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.post.request.UpdatePostRequestDto;
import com.skillbox.socialnetwork.main.dto.post.response.PostResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.model.Tag;
import com.skillbox.socialnetwork.main.repository.PostRepository;
import com.skillbox.socialnetwork.main.repository.TagRepository;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository repository, TagRepository tagRepository) {
        this.postRepository = repository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Post findById(Integer id) {
        return postRepository.findPostById(id);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public BaseResponseList feeds(int offset, int limit, Person person) {
        return PostResponseFactory.getPostsList(
                postRepository.getFeeds(PageRequest.of(offset, limit)), //TODO исправить!
                postRepository.getCountNotBlockedPost(),
                offset,
                limit,
                person);
    }

    @Override
    public BaseResponse getPost(int id, Person person) {
        return PostResponseFactory.getSinglePost(findById(id), person);
    }

    @Override
    public BaseResponse editPost(int id, Long publishDate, UpdatePostRequestDto request, Person person) {
        Post post = postRepository.findPostById(id);
        post.setPostText(request.getPostText());
        post.setTitle(request.getTitle());
        post.setTime(publishDate == null ? new Date() : new Date(publishDate));

        //tags
        List<Tag> tags = new ArrayList<>();
        if (request.getTags().size() != 0) {            //если тегов нет в запросе, блок пропускается
            request.getTags().forEach(tag -> {
                Tag postTag;
                if (tagRepository.existsByTagIgnoreCase(tag)) {
                    postTag = tagRepository.findFirstByTagIgnoreCase(tag);
                } else {
                    postTag = new Tag();
                    postTag.setTag(tag);
                }
                tags.add(postTag);
            });
            post.setTags(tags);
        }
        Post result = postRepository.save(post);
        return PostResponseFactory.getSinglePost(result, person);
    }

    @Override
    public BaseResponse deletePost(int id) {
        postRepository.delete(postRepository.findPostById(id));
        return ResponseFactory.responseOk();
    }

    @Override
    public BaseResponseList searchPosts(String text, Long dateFrom, Long dateTo, String author, int offset, int limit, Person person) {
        return PostResponseFactory.getPostsListWithLimit(
                postRepository.searchPosts(text, new Date(dateFrom), new Date(dateTo), author),
                offset, limit, person);
    }
}
