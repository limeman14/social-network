package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Friendship;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.enumerated.FriendshipCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query("select f from Friendship f where f.srcPerson=?1 and f.dstPerson=?2 and f.status.code=?3")
    Friendship findRelation(Person srcPerson, Person dstPerson, FriendshipCode code);

    @Query("select f from Friendship f where f.srcPerson=?1 and f.dstPerson=?2 and f.status.code<>'BLOCKED'")
    Friendship findNonBLockedRelation(Person srcPerson, Person dstPerson);


    @Query("select f from Friendship f where f.srcPerson=?1 and f.status.code='FRIEND'")
    List<Friendship> findAllFriends(Person person);

    @Query("select f from Friendship f where f.dstPerson=?1 and f.status.code='REQUEST'")
    List<Friendship> findAllRequests(Person person);

    @Query("select case when count(f)>0 then true else false end from Friendship f " +
            "where f.srcPerson=?1 and f.dstPerson=?2 and f.status.code=BLOCKED'")
    Boolean isBlocked(Person srcPerson, Person dstPerson);

    Friendship findBySrcPersonAndDstPerson(Person srcPerson, Person dstPerson);
}
