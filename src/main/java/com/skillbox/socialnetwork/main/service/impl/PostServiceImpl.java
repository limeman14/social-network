package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.repository.PostRepository;
import com.skillbox.socialnetwork.main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository repository;

    @Autowired
    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Post> getAll() {
        return repository.findAll();
    }

    @Override
    public Post findById(Integer id) {
        return repository.findPostById(id);
    }
}
