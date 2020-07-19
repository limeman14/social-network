package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.model.Friendship;
import com.skillbox.socialnetwork.main.model.FriendshipStatus;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.enumerated.FriendshipCode;
import com.skillbox.socialnetwork.main.repository.FriendshipRepository;
import com.skillbox.socialnetwork.main.repository.FriendshipStatusRepo;
import com.skillbox.socialnetwork.main.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
        Set<Person> friendSet = new HashSet<>();
        friendSet.addAll(person.getFriendshipsDst().stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipCode.FRIEND))
                .map(f -> f.getSrcPerson())
                .collect(Collectors.toSet()));
        friendSet.addAll(person.getFriendshipsSrc().stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipCode.FRIEND))
                .map(f -> f.getDstPerson())
                .collect(Collectors.toSet()));
        List<Person> resultList = new ArrayList<>();
        resultList.addAll(friendSet);
        return resultList;
    }

    @Override
    public List<Person> getFriendRequest(Person person, String name) {
        Set<Person> friendsSet = new HashSet<>();
        friendsSet.addAll(person.getFriendshipsDst().stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipCode.REQUEST))
                .map(f -> f.getSrcPerson())
                .filter(p -> p.getFirstName().contains(name) || p.getLastName().contains(name))
                .collect(Collectors.toList()));
        friendsSet.addAll(person.getFriendshipsSrc().stream()
                .filter(f -> f.getStatus().getCode().equals(FriendshipCode.REQUEST))
                .map(f -> f.getDstPerson())
                .filter(p -> p.getFirstName().contains(name) || p.getLastName().contains(name))
                .collect(Collectors.toList()));
        List<Person> resultList = new ArrayList<>();
        resultList.addAll(friendsSet);
        return resultList;
    }

    @Override
    public String addFriend(Person srcPerson, Person dstPerson) {
        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setName("name");
        friendshipStatus.setTime(new Date());
        Friendship friendship = new Friendship();
        friendship.setSrcPerson(srcPerson);
        friendship.setDstPerson(dstPerson);
        //@TODO: Сделать проверку на isBlocked
        if (srcPerson.getFriendshipsDst().contains(dstPerson)) {
            friendshipStatus.setCode(FriendshipCode.FRIEND);
        } else {
            friendshipStatus.setCode(FriendshipCode.REQUEST);
        }
        friendship.setStatus(friendshipStatus);
        friendshipStatusRepo.save(friendshipStatus);
        friendshipRepository.save(friendship);

        return "ok";
    }

//    public String deleteFriend(Person owner, Person deletedFriend) {
//        FriendshipStatus status = friendshipStatusRepo.
//        friendshipStatusRepo.
//    }
}
