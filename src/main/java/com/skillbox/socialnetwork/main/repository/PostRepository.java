package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Post findPostById(Integer id);

    @Query(value = "select * from posts ORDER BY time desc limit ?1, ?2", nativeQuery = true)
    List<Post> limitQuery(Integer offset, Integer limit);
}
