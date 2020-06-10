package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.post.request.UpdatePostRequestDto;
import com.skillbox.socialnetwork.main.dto.post.response.PostResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.MessageResponseDto;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.model.Post2tag;
import com.skillbox.socialnetwork.main.model.Tag;
import com.skillbox.socialnetwork.main.repository.P2TRepository;
import com.skillbox.socialnetwork.main.repository.PostRepository;
import com.skillbox.socialnetwork.main.repository.TagRepository;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final P2TRepository p2TRepository;

    @Autowired
    public PostServiceImpl(PostRepository repository, TagRepository tagRepository, P2TRepository p2TRepository) {
        this.postRepository = repository;
        this.tagRepository = tagRepository;
        this.p2TRepository = p2TRepository;
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
    public BaseResponseList feeds(int offset, int limit) {
        return PostResponseFactory.getPostsList(postRepository.limitQuery(offset, limit)); //тут было интересное решение с limitQuery
    }

    @Override
    public BaseResponse getPost(int id) {
        return PostResponseFactory.getSinglePost(findById(id));
    }

    @Override
    public BaseResponse editPost(int id, Long publishDate, UpdatePostRequestDto request) {
        Post post = postRepository.findPostById(id);
        post.setPostText(request.getPostText());
        post.setTitle(request.getTitle());
        post.setTime(publishDate == null ? new Date() : new Date(publishDate));

        //tags
        List<Post2tag> tags = new ArrayList<>();
        if (request.getTags() != null) {            //если тегов новых не прислали, блок пропускается
            request.getTags().forEach(tag -> {
                Post2tag ttp = new Post2tag();
                ttp.setPost(post);
                if (!tagRepository.existsByTagIgnoreCase(tag.getTag())) {
                    Tag t = new Tag();
                    t.setTag(tag.getTag());
                    tagRepository.save(t);
                }
                ttp.setTag(tagRepository.findFirstByTagIgnoreCase(tag.getTag()));
                tags.add(ttp);
            });
            post.setTags(p2TRepository.saveAll(tags));
        }
        Post result = postRepository.save(post);
        return PostResponseFactory.getSinglePost(result);
    }

    @Override
    public BaseResponse deletePost(int id) {
        postRepository.delete(postRepository.findPostById(id));
        return new BaseResponse(new MessageResponseDto("ok"));
    }
}
