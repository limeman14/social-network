package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.model.Person;

import java.util.List;

public interface FriendsService {
    List<Person> getFriends(Person person, String name);

    List<Person> getFriendRequest(Person person, String name);

    String addFriend(Person srcPerson, Person dstPerson);

    String deleteFriend(Person owner, Person deletedFriend);
}