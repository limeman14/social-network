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

import java.util.Date;
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
        return friendshipRepository.findAllFriends(person).stream()
                .map(f -> f.getDstPerson())
                .filter(f -> f.getFirstName().contains(name)
                        || f.getLastName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> getFriendRequest(Person person, String name) {
        return friendshipRepository.findAllRequests(person).stream()
                .map(f -> f.getSrcPerson())
                .filter(f -> f.getFirstName().contains(name)
                        || f.getLastName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public String addFriend(Person srcPerson, Person dstPerson) {
        Friendship friendshipSrc = friendshipRepository.findBySrcPersonAndDstPerson(srcPerson, dstPerson);
        Friendship friendshipDst = friendshipRepository.findBySrcPersonAndDstPerson(dstPerson, srcPerson);
        if (friendshipDst != null && (friendshipDst.getStatus().getCode().equals(FriendshipCode.REQUEST)
                || friendshipDst.getStatus().getCode().equals(FriendshipCode.SUBSCRIBED))) {
            friendshipDst.getStatus().setCode(FriendshipCode.FRIEND);
            friendshipDst.getStatus().setTime(new Date());
            if (friendshipSrc == null) {
                friendshipSrc = new Friendship(new FriendshipStatus(), srcPerson, dstPerson);
            }
            friendshipSrc.getStatus().setCode(FriendshipCode.FRIEND);
            friendshipSrc.getStatus().setTime(new Date());
            friendshipStatusRepo.save(friendshipDst.getStatus());
            friendshipStatusRepo.save(friendshipSrc.getStatus());
            friendshipRepository.save(friendshipSrc);
            friendshipRepository.save(friendshipDst);
        } else if (friendshipSrc != null) {
            if (friendshipSrc.getStatus().getCode().equals(FriendshipCode.REQUEST)) {
            }
        } else {
            FriendshipStatus status = new FriendshipStatus(new Date(), FriendshipCode.REQUEST);
            friendshipSrc = new Friendship(status, srcPerson, dstPerson);
            friendshipStatusRepo.save(status);
            friendshipRepository.save(friendshipSrc);
        }
        return "ok";
    }

    public String deleteFriend(Person owner, Person deletedFriend) {
        Friendship relationsSrc = friendshipRepository.findBySrcPersonAndDstPerson(owner, deletedFriend);
        Friendship relationsDst = friendshipRepository.findBySrcPersonAndDstPerson(deletedFriend, owner);
        if (relationsSrc != null && relationsDst != null
                && relationsSrc.getStatus().getCode().equals(FriendshipCode.FRIEND)
                && relationsSrc.getStatus().getCode().equals(FriendshipCode.FRIEND)) {
            relationsSrc.getStatus().setCode(FriendshipCode.DECLINED);
            relationsDst.getStatus().setCode(FriendshipCode.SUBSCRIBED);
            relationsSrc.getStatus().setTime(new Date());
            relationsDst.getStatus().setTime(new Date());
            friendshipStatusRepo.save(relationsDst.getStatus());
            friendshipStatusRepo.save(relationsSrc.getStatus());
        }
        return "ok";
    }
}
