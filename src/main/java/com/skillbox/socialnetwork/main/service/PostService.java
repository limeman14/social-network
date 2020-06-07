package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.universal.BaseResponseListDto;
import com.skillbox.socialnetwork.main.model.Post;

import java.util.List;

public interface PostService {
    List<Post> getAll();

    Post findById(Integer id);

    Post save(Post post);

    BaseResponseListDto feeds(int offset, int limit);
}
