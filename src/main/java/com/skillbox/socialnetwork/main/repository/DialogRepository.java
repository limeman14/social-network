package com.skillbox.socialnetwork.main.repository;

import com.skillbox.socialnetwork.main.model.Dialog;
import com.skillbox.socialnetwork.main.model.Message;
import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Integer> {
    @Query("select dtp.dialog from DialogToPerson dtp where dtp.person=?1 and dtp.person.lastName like %?2%")
    List<Dialog> getDialogs(Person person, String query);

    @Query("select count(m) from Message m " +
            "join Dialog d on m.dialog.id=d.id " +
            "join DialogToPerson dtp on dtp.dialog.id=d.id " +
            "where m.readStatus=com.skillbox.socialnetwork.main.model.enumerated.ReadStatus.SENT " +
            "and m.recipient=?1")
    int countUnreadMessages(Person person);

    @Query("select count(m) from Message m " +
            "join Dialog d on m.dialog.id=d.id " +
            "join DialogToPerson dtp on dtp.dialog.id=d.id " +
            "join Person p on p.id = dtp.person.id " +
            "where p = ?1 and d = ?2")
    void deleteUserFromDialog(Person person, Dialog dialog);
}
