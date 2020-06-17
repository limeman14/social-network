package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.post.request.UpdatePostRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.List;

public interface PostService {
    Post findById(Integer id);

    Post save(Post post);

    BaseResponseList feeds(int offset, int limit);

    BaseResponse getPost(int id);

    BaseResponse editPost(int id, Long publishDate, UpdatePostRequestDto request);

    BaseResponse deletePost(int id);

    BaseResponseList searchPosts(String text, Long dateFrom, Long dateTo, String author, int offset, int limit);
}
