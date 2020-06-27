package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.enumerated.FriendshipCode;
import com.skillbox.socialnetwork.main.repository.FriendshipRepository;
import com.skillbox.socialnetwork.main.repository.FriendshipStatusRepo;
import com.skillbox.socialnetwork.main.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendsServiceImpl implements FriendsService {
    private final FriendshipRepository friendshipRepository;
    private final FriendshipStatusRepo friendshipStatusRepo;

    @Autowired
    public FriendsServiceImpl(FriendshipRepository friendshipRepository, FriendshipStatusRepo friendshipStatusRepo) {
        this.friendshipRepository = friendshipRepository;
        this.friendshipStatusRepo = friendshipStatusRepo;
    }

    @Override
    public List<Person> getFriends(Person person, String name) {
        List<Person> srcList = person.getFriendshipsSrc().stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipCode.FRIEND))
                .map(f -> f.getDstPerson())
                .filter(f -> f.getFirstName().contains(name)
                        || f.getLastName().contains(name))
                .collect(Collectors.toList());
        List<Person> dstList = person.getFriendshipsDst().stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipCode.FRIEND))
                .map(f -> f.getSrcPerson())
                .filter(f -> f.getFirstName().contains(name)
                        || f.getLastName().contains(name))
                .collect(Collectors.toList());
        List<Person> result = new ArrayList<>();
        result.addAll(srcList);
        result.addAll(dstList);
        return result;
    }
}
