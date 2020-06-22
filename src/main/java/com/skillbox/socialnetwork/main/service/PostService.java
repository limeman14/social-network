package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.post.request.UpdatePostRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Post;

public interface PostService {
    Post findById(Integer id);

    Post save(Post post);

    BaseResponseList feeds(int offset, int limit, Person person);

    BaseResponse getPost(int id, Person person);

    BaseResponse editPost(int id, Long publishDate, UpdatePostRequestDto request, Person person);

    BaseResponse deletePost(int id);

    BaseResponseList searchPosts(String text, Long dateFrom, Long dateTo, String author, int offset, int limit, Person person);
}
