package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findPostById(Integer id);
}
