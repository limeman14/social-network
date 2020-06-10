package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Message;
import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> getAllByMessageTextAndAuthorOrderByTimeDesc(String query, Person author);
}
