package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.dialog.DialogAddRequest;
import com.skillbox.socialnetwork.main.dto.dialog.DialogFactory;
import com.skillbox.socialnetwork.main.dto.dialog.LongpollHistoryRequest;
import com.skillbox.socialnetwork.main.dto.dialog.response.*;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.model.*;
import com.skillbox.socialnetwork.main.model.enumerated.NotificationCode;
import com.skillbox.socialnetwork.main.model.enumerated.ReadStatus;
import com.skillbox.socialnetwork.main.repository.D2PRepository;
import com.skillbox.socialnetwork.main.repository.DialogRepository;
import com.skillbox.socialnetwork.main.repository.MessageRepository;
import com.skillbox.socialnetwork.main.repository.NotificationRepository;
import com.skillbox.socialnetwork.main.service.DialogService;
import com.skillbox.socialnetwork.main.service.NotificationService;
import com.skillbox.socialnetwork.main.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class DialogServiceImpl implements DialogService {

    private final MessageRepository messageRepository;
    private final PersonService personService;
    private final DialogRepository dialogRepository;
    private final D2PRepository d2pRepository;
    private final NotificationService notificationService;

    @Autowired
    public DialogServiceImpl(MessageRepository messageRepository, PersonService personService, DialogRepository dialogRepository, D2PRepository d2pRepository, NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.personService = personService;
        this.dialogRepository = dialogRepository;
        this.d2pRepository = d2pRepository;
        this.notificationService = notificationService;
    }

    @Override
    public BaseResponseList getDialogs(String query, int offset, int limit, Person person) {
        return DialogFactory.getDialogs(dialogRepository.getDialogs(person, query), person, offset, limit);
    }

    @Override
    public BaseResponse addDialog(DialogAddRequest request, Person currentUser) {
        AtomicBoolean dialogAlreadyExists = new AtomicBoolean(false);
        AtomicInteger dialogId = new AtomicInteger();
        currentUser.getDialogToPeople().stream().map(DialogToPerson::getDialog).map(Dialog::getDialogToPersonList).forEach(dialogToPeople -> dialogAlreadyExists
                .set(dialogToPeople.stream().anyMatch(dialogToPerson -> {
                    dialogId.set(dialogToPerson.getDialog().getId());
                    return dialogToPerson.getPerson().getId()
                            .equals(request.getUserIds().get(0));
                })));
        if (dialogAlreadyExists.get()) {
            return new BaseResponse(new IdDto(dialogId.get()));
        } else {//if not exists
            Dialog d = new Dialog();
            d.setDialogToPersonList(new ArrayList<>());
            d.setMessages(new ArrayList<>());
            Dialog dialog = dialogRepository.save(d);
            List<Person> result = request
                    .getUserIds()
                    .stream()
                    .map(personService::findById)
                    .collect(Collectors.toList());
            result.add(currentUser);
            List<DialogToPerson> dtps = d2pRepository.saveAll(result
                    .stream()
                    .map(person -> {
                        DialogToPerson dtp = new DialogToPerson();
                        dtp.setDialog(dialog);
                        dtp.setPerson(person);
                        return dtp;
                    })
                    .collect(Collectors.toList()));
            dialog.setDialogToPersonList(dtps);
            Dialog saved = dialogRepository.save(dialog);

            //dialog initializer
            Message message = new Message();
            message.setMessageText("Пользователь " + currentUser.getFirstName() + " " + currentUser.getLastName() + " начал чат.");
            message.setAuthor(currentUser);
            message.setRecipient(personService.findById(request.getUserIds().get(0)));
            message.setReadStatus(ReadStatus.SENT);
            message.setTime(new Date());
            message.setDialog(saved);
            messageRepository.save(message);
            saved.getMessages().add(message);
            saved = dialogRepository.save(saved);


            log.info("IN addDialog Dialog: {} added", saved.getId());
            return new BaseResponse(new IdDto(saved.getId()));
        }
    }

    @Override
    public BaseResponse getUnreadMessages(Person person) {
        return new BaseResponse(new CountDto(dialogRepository.countUnreadMessages(person)));
    }

    @Override
    public BaseResponse deleteDialog(int dialogId) {
        dialogRepository.deleteById(dialogId);
        log.info("Dialog with id {} is deleted", dialogId);
        return new BaseResponse(new IdDto(dialogId));
    }

    @Override
    public BaseResponse addUsersToDialog(DialogAddRequest request) {
        return addDialog(request, null);
    }

    @Override
    public BaseResponse deleteUsersFromDialog(int dialogId, DialogAddRequest request) {
        Dialog dialog = dialogRepository
                .findById(dialogId)
                .orElse(null);
        DialogToPerson dtp = new DialogToPerson();
        request
                .getUserIds()
                .stream()
                .map(personService::findById)
                .collect(Collectors.toList())
                .forEach(o -> dialogRepository.deleteUserFromDialog(o, dialog));
        return new BaseResponse(request);
    }

    @Override
    public BaseResponse inviteUserToDialog(int id) {//Как линк сгенерировать??
        return null;
    }

    @Override
    public BaseResponse joinDialog(int id, LinkDto linkDto) {//аналогично
        return null;
    }

    @Override
    public BaseResponseList getMessagesFromDialog(int id, String query, int offset, int limit, int fromMessageId, Person currentUser) {
        Dialog dialog = dialogRepository.findById(id).orElse(null);
        if (dialog != null) {
            List<Message> list = messageRepository.getAllByMessageTextContainingAndDialog(query, dialog);
            int lastElementIndex = list.size();
            int firstElementIndex = list.size() - limit;
            if (offset != 0){
                //достаем локальный индекс сообщения от которого будем грузить старые
                Message fromMessage = messageRepository.findById(fromMessageId).get();
                int indexOfFromMessage = list.indexOf(fromMessage);
                list = list.subList(0, indexOfFromMessage);
                lastElementIndex = list.size() - 1;
                firstElementIndex = lastElementIndex - limit;
            }
            if (firstElementIndex >= 0){
                list = list.subList(firstElementIndex, lastElementIndex);
            }
            else if (lastElementIndex > 0) {
                list = list.subList(0, lastElementIndex);
            }
            else list = new ArrayList<>();
            messageRepository.saveAll(list
                    .stream()
                    .filter(message -> message.getRecipient().equals(currentUser))
                    .peek(message -> message.setReadStatus(ReadStatus.READ))
                    .collect(toList()));
            return DialogFactory.getMessages(list, currentUser, offset, limit, fromMessageId);
        } else {
            return null;
        }
    }

    @Override
    public BaseResponse addMessage(int dialogId, MessageTextDto request, Person user) {
        Dialog dialog = dialogRepository.findById(dialogId).orElse(null);
        if (dialog == null) {
            log.error("Adding message failed, dialog {} is null", dialogId);
            throw new NullPointerException("Dialog cannot be null");
        }
        Message message = new Message();
        Person dstPerson = dialog.getDialogToPersonList().stream().filter(dtp -> !dtp.getPerson().equals(user))
                .findFirst().get().getPerson();
        message.setAuthor(user);
        message.setRecipient(dstPerson);
        message.setTime(new Date());
        message.setMessageText(request.getText());
        message.setReadStatus(ReadStatus.SENT);
        message.setDialog(dialog);

        //уведомление о сообщении
        notificationService.addNotification(user, dstPerson, NotificationCode.MESSAGE, message.getMessageText());
        log.info("SENT MESSAGE notification to " + dstPerson.getFirstName() + " " + dstPerson.getLastName());

        return new BaseResponse(DialogFactory.formatMessage(messageRepository.save(message), user));
    }

    @Override
    public BaseResponse deleteMessage(int dialogId, int messageId) {
        messageRepository.deleteById(messageId);
        log.info("Message with id {} is deleted", messageId);
        return new BaseResponse(new MessageIdDto(messageId));
    }

    @Override
    public BaseResponse editMessage(int dialogId, int messageId, MessageTextDto messageText, Person user) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            log.error("Message with id {} not found", messageId);
            throw new NullPointerException("Message not with id:" + messageId + " found");
        }
        message.setMessageText(messageText.getText());

        return new BaseResponse(DialogFactory.formatMessage(messageRepository.save(message), user));
    }

    @Override
    public BaseResponse recoverMessage(int dialogId, int messageId) {
        return null;//Как?
    }

    @Override
    public BaseResponse markMessageAsRead(int dialogId, int messageId, Person person) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null)
            throw new NullPointerException("Message with id: " + messageId + " not found");
        message.setReadStatus(ReadStatus.READ);
        messageRepository.save(message);
        log.info("Message {} marked as READ", message.getMessageText());
        return ResponseFactory.responseOk();
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
