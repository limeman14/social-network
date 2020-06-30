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
        return friendshipRepository.findAllFriends(person).stream()
                .map(f -> f.getDstPerson())
                .filter(f -> f.getFirstName().contains(name)
                        || f.getLastName().contains(name))
                .collect(Collectors.toList());
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
        FriendshipStatus friendshipStatusSrc = new FriendshipStatus(new Date(), FriendshipCode.REQUEST);
        Friendship friendshipSrc = friendshipRepository.findBySrcPersonAndDstPerson(srcPerson, dstPerson) == null
                ? new Friendship(friendshipStatusSrc, srcPerson, dstPerson)
                : friendshipRepository.findBySrcPersonAndDstPerson(srcPerson, dstPerson);
        Friendship friendshipDst = friendshipRepository.findBySrcPersonAndDstPerson(dstPerson, srcPerson) == null
                ? new Friendship(new FriendshipStatus(), dstPerson, srcPerson)
                : friendshipRepository.findBySrcPersonAndDstPerson(dstPerson, srcPerson);
        if (friendshipDst.getStatus().getCode() != null) {
            if (friendshipDst.getStatus().getCode().equals(FriendshipCode.FRIEND))
                return "ok";
            if (friendshipDst.getStatus().getCode().equals(FriendshipCode.REQUEST)) {
                FriendshipStatus friendshipStatusDst = new FriendshipStatus(new Date(), FriendshipCode.FRIEND);
                friendshipStatusSrc.setCode(FriendshipCode.FRIEND);
                friendshipSrc.setStatus(friendshipStatusSrc);
                friendshipDst.setStatus(friendshipStatusDst);
                friendshipStatusRepo.save(friendshipStatusSrc);
                friendshipStatusRepo.save(friendshipStatusDst);
                friendshipRepository.save(friendshipDst);
                friendshipRepository.save(friendshipSrc);
            }
        } else {
            friendshipStatusRepo.save(friendshipStatusSrc);
            friendshipRepository.save(friendshipSrc);
        }
//        if (friendshipDst != null || friendshipSrc != null) {
//            if (friendshipDst.getStatus().getCode().equals(FriendshipCode.REQUEST)) {
//                friendshipDst.getStatus().setCode(FriendshipCode.FRIEND);
//                friendshipSrc.getStatus().setCode(FriendshipCode.FRIEND);
//                friendshipDst.getStatus().setTime(new Date());
//                friendshipSrc.getStatus().setTime(new Date());
//            }
//        } else {
//            FriendshipStatus friendshipStatus = new FriendshipStatus();
//            friendshipStatus.setCode(FriendshipCode.REQUEST);
//            friendshipStatus.setTime(new Date());
//            friendshipSrc.setStatus(friendshipStatus);
//
//            friendshipStatusRepo.save(friendshipStatus);
//            friendshipRepository.save(friendshipSrc);
//        }
//        FriendshipStatus friendshipStatusSrc = new FriendshipStatus();
//        FriendshipStatus friendshipStatusDst = new FriendshipStatus();
//        friendshipStatusSrc.setTime(new Date());
//        friendshipStatusDst.setTime(new Date());
//        Friendship friendship = new Friendship();
//        friendship.setSrcPerson(srcPerson);
//        friendship.setDstPerson(dstPerson);
//        //@TODO: Сделать проверку на isBlocked
//        if (srcPerson.getFriendshipsDst().contains(dstPerson)) {
//            friendshipStatus.setCode(FriendshipCode.FRIEND);
//        } else {
//            friendshipStatus.setCode(FriendshipCode.REQUEST);
//        }
//        friendship.setStatus(friendshipStatus);
//        friendshipStatusRepo.save(friendshipStatus);
//        friendshipRepository.save(friendship);

        return "ok";
    }
//
//    public String deleteFriend(Person owner, Person deletedFriend) {
//        FriendshipStatus status = friendshipStatusRepo.
//                friendshipStatusRepo.
//    }
}
