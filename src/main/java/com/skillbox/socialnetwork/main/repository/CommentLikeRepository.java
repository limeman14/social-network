package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
}
