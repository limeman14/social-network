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
import com.skillbox.socialnetwork.main.service.PersonService;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PersonService personService;

    @Autowired
    public PostServiceImpl(PostRepository repository, TagRepository tagRepository, PersonService personService) {
        this.postRepository = repository;
        this.tagRepository = tagRepository;
        this.personService = personService;
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
        int count = postRepository.getCountNotBlockedPost();
        int page = offset / limit;
        return PostResponseFactory.getPostsList(
                postRepository.getFeeds(PageRequest.of(page, limit)),
                count,
                offset,
                limit,
                person);
    }

    @Override
    public BaseResponse getPost(int id, int personId) {
        return PostResponseFactory.getSinglePost(findById(id), personService.findById(personId));
    }

    @Override
    public BaseResponse editPost(int id, Long publishDate, UpdatePostRequestDto request, int personId) {
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
        return PostResponseFactory.getSinglePost(result, personService.findById(personId));
    }

    @Override
    public BaseResponse deletePost(int id) {
        postRepository.delete(postRepository.findPostById(id));
        return ResponseFactory.responseOk();
    }

    @Override
    public BaseResponseList searchPosts(String text, Long dateFrom, Long dateTo, String author, String tagsRequest, int offset, int limit, int personId) {
        List<String> tags = Arrays.asList(tagsRequest.split(","));
//        if(tags.size()>0){
//                result = result.stream().filter(post -> {
//                    boolean flag = false;
//                    for(Tag tag:post.getTags()){
//                        for(String queryTag: tags){
//                            if(tag.getTag().equalsIgnoreCase(queryTag)){
//                                flag = true;
//                            }
//                        }
//                    }
//                    return flag;
//                }).collect(Collectors.toList());
//            }
        List<Post> result = tagsRequest.length()>0 && tags.size()!=0
                ? postRepository.searchPostsWithTags(text, new Date(dateFrom), new Date(dateTo), author, tags)
                : postRepository.searchPosts(text, new Date(dateFrom), new Date(dateTo), author);
        return PostResponseFactory.getPostsListWithLimit(
                result, offset, limit, personService.findById(personId));
    }
}
