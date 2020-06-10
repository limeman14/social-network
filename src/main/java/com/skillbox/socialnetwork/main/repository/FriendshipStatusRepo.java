package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipStatusRepo extends JpaRepository<FriendshipStatus, Integer> {
}
