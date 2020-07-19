package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.DialogToPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface D2PRepository extends JpaRepository<DialogToPerson, Integer> {
}
