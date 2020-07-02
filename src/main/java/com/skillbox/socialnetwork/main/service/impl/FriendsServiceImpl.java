package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.model.Friendship;
import com.skillbox.socialnetwork.main.model.FriendshipStatus;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.enumerated.FriendshipCode;
import com.skillbox.socialnetwork.main.model.enumerated.NotificationCode;
import com.skillbox.socialnetwork.main.repository.FriendshipRepository;
import com.skillbox.socialnetwork.main.repository.FriendshipStatusRepo;
import com.skillbox.socialnetwork.main.service.FriendsService;
import com.skillbox.socialnetwork.main.service.NotificationService;
import com.skillbox.socialnetwork.main.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendsServiceImpl implements FriendsService {
    private final FriendshipRepository friendshipRepository;
    private final FriendshipStatusRepo friendshipStatusRepo;
    private final NotificationService notificationService;
    private final PersonService personService;

    @Autowired
    public FriendsServiceImpl(FriendshipRepository friendshipRepository, FriendshipStatusRepo friendshipStatusRepo, NotificationService notificationService, PersonService personService)
    {
        this.friendshipRepository = friendshipRepository;
        this.friendshipStatusRepo = friendshipStatusRepo;
        this.notificationService = notificationService;
        this.personService = personService;
    }

    @Override
    public List<Person> getFriends(Person person, String name)
    {
        return friendshipRepository.findAllFriends(person).stream()
                .map(f -> f.getDstPerson())
                .filter(f -> f.getFirstName().contains(name)
                        || f.getLastName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> getFriendRequest(Person person, String name)
    {
        return friendshipRepository.findAllRequests(person).stream()
                .map(f -> f.getSrcPerson())
                .filter(f -> f.getFirstName().contains(name)
                        || f.getLastName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public String addFriend(Person srcPerson, Person dstPerson) {
        /*
        Добавление в друзья: Текущий пользователь srcPerson, целевой пользователь dstPerson.
        Принятие запроса в друзья: Тот, кто подтверждает или отклонияет запрос в друзья srcPerson,
        тот, кто отправлял этот запрос - dstPerson
         */
        Friendship friendshipSrc = friendshipRepository.findNonBLockedRelation(srcPerson, dstPerson);
        Friendship friendshipDst = friendshipRepository.findNonBLockedRelation(dstPerson, srcPerson);
        if (friendshipDst != null && (friendshipDst.getStatus().getCode().equals(FriendshipCode.REQUEST)
                || friendshipDst.getStatus().getCode().equals(FriendshipCode.SUBSCRIBED)))
        {
            friendshipDst.getStatus().setCode(FriendshipCode.FRIEND);
            friendshipDst.getStatus().setTime(new Date());
            if (friendshipSrc == null)
            {
                friendshipSrc = new Friendship(new FriendshipStatus(), srcPerson, dstPerson);
            }
            friendshipSrc.getStatus().setCode(FriendshipCode.FRIEND);
            friendshipSrc.getStatus().setTime(new Date());
            friendshipStatusRepo.save(friendshipDst.getStatus());
            friendshipStatusRepo.save(friendshipSrc.getStatus());
            friendshipRepository.save(friendshipSrc);
            friendshipRepository.save(friendshipDst);

            //Notification
            notificationService.addNotification(srcPerson, dstPerson, NotificationCode.FRIEND_REQUEST,
                    srcPerson.getFirstName() + " " + srcPerson.getLastName() + " добавил Вас в друзья.");
    } else if (friendshipSrc != null) {
            if (friendshipSrc.getStatus().getCode().equals(FriendshipCode.REQUEST)) {
                log.warn("addFriend failed from {} to {} friend request is already sent",
                        srcPerson.getEmail(),
                        dstPerson.getEmail());
                return "";
            }
            if (friendshipSrc.getStatus().getCode().equals(FriendshipCode.DECLINED)){
                log.warn("addFriend failed from {} to {}, target user declined source user's request",
                        srcPerson.getEmail(),
                        dstPerson.getEmail());
                return "";
            }
            if (friendshipSrc.getStatus().getCode().equals(FriendshipCode.BLOCKED)){
                log.warn("addFriend failed from {} to {}, target user blocked source user",
                        srcPerson.getEmail(),
                        dstPerson.getEmail());
                return "";
            }
        } else {
            FriendshipStatus status = new FriendshipStatus(new Date(), FriendshipCode.REQUEST);
            friendshipSrc = new Friendship(status, srcPerson, dstPerson);
            friendshipStatusRepo.save(status);
            friendshipRepository.save(friendshipSrc);

            notificationService.addNotification(srcPerson, dstPerson, NotificationCode.FRIEND_REQUEST,
                    srcPerson.getFirstName() + " " + srcPerson.getLastName() + " добавил Вас в друзья.");
        }
        return "ok";
    }


    @Override
    public Map<Person, String> isFriend(Person srcPerson, List<Person> dstPersonList) {
        Map<Person, String> isFriendMap = new HashMap<>();
        for (int i = 0; i < dstPersonList.size(); i++) {
            Person dstPerson = dstPersonList.get(i);
            Friendship friendshipSrc = friendshipRepository
                    .findRelation(srcPerson, dstPerson, FriendshipCode.FRIEND);
            if (friendshipSrc != null){
                isFriendMap.put(dstPerson, friendshipSrc.getStatus().getCode().toString());
            }
        }
        return isFriendMap;
    }

    public String deleteFriend(Person owner, Person deletedFriend) {
        Friendship relationsSrc = friendshipRepository.findNonBLockedRelation(owner, deletedFriend);
        Friendship relationsDst = friendshipRepository.findNonBLockedRelation(deletedFriend, owner);
        if (relationsSrc != null
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
