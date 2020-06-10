package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.dialog.DialogAddRequest;
import com.skillbox.socialnetwork.main.dto.dialog.DialogFactory;
import com.skillbox.socialnetwork.main.dto.dialog.LongpollHistoryRequest;
import com.skillbox.socialnetwork.main.dto.dialog.response.LinkDto;
import com.skillbox.socialnetwork.main.dto.dialog.response.MessageTextDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.model.Message;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.repository.MessageRepository;
import com.skillbox.socialnetwork.main.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DialogServiceImpl implements DialogService {

    private final MessageRepository messageRepository;

    @Autowired
    public DialogServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public BaseResponseList getAllMessages(String query, int offset, int limit, Person person) {
        return DialogFactory.getMessages(messageRepository.getAllByMessageTextAndAuthorOrderByTimeDesc(query, person), offset, limit);
    }

    @Override
    public BaseResponse addDialog(DialogAddRequest request) {
        return null;
    }

    @Override
    public BaseResponse getUnreadMessages(Person person) {
        return null;
    }

    @Override
    public BaseResponse deleteDialog(int dialogId) {
        return null;
    }

    @Override
    public BaseResponse addUsersToDialog(DialogAddRequest request) {
        return null;
    }

    @Override
    public BaseResponse deleteUsersFromDialog(DialogAddRequest request) {
        return null;
    }

    @Override
    public BaseResponse inviteUserToDialog(int id) {
        return null;
    }

    @Override
    public BaseResponse joinDialog(int id, LinkDto linkDto) {
        return null;
    }

    @Override
    public BaseResponseList getMessagesFromDialog(int id, String query, int offset, int limit) {
        return null;
    }

    @Override
    public BaseResponse addMessage(int dialogId, MessageTextDto message) {
        return null;
    }

    @Override
    public BaseResponse deleteMessage(int dialogId, int messageId) {
        return null;
    }

    @Override
    public BaseResponse editMessage(int dialogId, int messageId, MessageTextDto messageText) {
        return null;
    }

    @Override
    public BaseResponse recoverMessage(int dialogId, int messageId) {
        return null;
    }

    @Override
    public BaseResponse markMessageAsRead(int dialogId, int messageId) {
        return null;
    }

    @Override
    public BaseResponse getUserActivityStatus(int dialogId, int userId) {
        return null;
    }

    @Override
    public BaseResponse changeUserActivityStatus(int dialogId, int userId) {
        return null;
    }

    @Override
    public BaseResponse getLongPollCredentials() {
        return null;
    }

    @Override
    public BaseResponse getLongPollHistory(LongpollHistoryRequest request) {
        return null;
    }
}
