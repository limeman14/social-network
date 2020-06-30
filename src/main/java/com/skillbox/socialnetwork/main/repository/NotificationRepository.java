package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    //стандартный метод репозитория удаления по ID не работал

    @Query("delete from Notification n where n = ?1")
    @Transactional
    @Modifying
    void deleteNotification(Notification notification);

}
