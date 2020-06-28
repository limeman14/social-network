package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
