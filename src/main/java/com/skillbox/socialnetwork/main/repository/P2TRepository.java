package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Post2tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface P2TRepository extends JpaRepository<Post2tag, Integer> {
}
