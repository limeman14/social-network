package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.notifications.request.NotificationSettingDto;
import com.skillbox.socialnetwork.main.dto.notifications.response.NotificationResponseFactory;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.MessageResponseDto;
import com.skillbox.socialnetwork.main.model.Notification;
import com.skillbox.socialnetwork.main.model.NotificationSettings;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.enumerated.NotificationCode;
import com.skillbox.socialnetwork.main.repository.NotificationRepository;
import com.skillbox.socialnetwork.main.repository.NotificationSettingsRepository;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final PersonRepository personRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;

    @Autowired
    public NotificationServiceImpl(PersonRepository personRepository, NotificationRepository notificationRepository, NotificationSettingsRepository notificationSettingsRepository) {
        this.personRepository = personRepository;
        this.notificationRepository = notificationRepository;
        this.notificationSettingsRepository = notificationSettingsRepository;
    }

    @Override
    public BaseResponseList getNotificationSettings(int userId) {
        List<NotificationSettingDto> settingsList = new ArrayList<>();
        NotificationSettings personSettings = notificationSettingsRepository.findByPersonId(userId);
        if (personSettings != null) {
            settingsList.add(new NotificationSettingDto(NotificationCode.POST, personSettings.isPostNotification()));
            settingsList.add(new NotificationSettingDto(NotificationCode.POST_COMMENT, personSettings.isPostCommentNotification()));
            settingsList.add(new NotificationSettingDto(NotificationCode.COMMENT_COMMENT, personSettings.isCommentCommentNotification()));
            settingsList.add(new NotificationSettingDto(NotificationCode.FRIEND_REQUEST, personSettings.isFriendRequestNotification()));
            settingsList.add(new NotificationSettingDto(NotificationCode.MESSAGE, personSettings.isMessageNotification()));
        }
        else {
            settingsList.add(new NotificationSettingDto(NotificationCode.POST, false));
            settingsList.add(new NotificationSettingDto(NotificationCode.POST_COMMENT, false));
            settingsList.add(new NotificationSettingDto(NotificationCode.COMMENT_COMMENT, false));
            settingsList.add(new NotificationSettingDto(NotificationCode.FRIEND_REQUEST, false));
            settingsList.add(new NotificationSettingDto(NotificationCode.MESSAGE, false));
        }
        return new BaseResponseList(settingsList);
    }

    @Override
    public BaseResponse changeNotificationSetting(int userId, NotificationSettingDto settingDto) {
        NotificationSettings notificationSettings = notificationSettingsRepository.findByPersonId(userId);
        if (notificationSettings == null) {
            notificationSettings = new NotificationSettings();
            notificationSettings.setPerson(personRepository.findPersonById(userId));
        }
        boolean flag = settingDto.getEnable();
        switch (settingDto.getNotificationType().name()) {
            case "POST" : notificationSettings.setPostNotification(flag); break;
            case "POST_COMMENT" : notificationSettings.setPostCommentNotification(flag); break;
            case "COMMENT_COMMENT" : notificationSettings.setCommentCommentNotification(flag); break;
            case "FRIEND_REQUEST" : notificationSettings.setFriendRequestNotification(flag); break;
            case "MESSAGE" : notificationSettings.setMessageNotification(flag); break;
        }
        notificationSettingsRepository.save(notificationSettings);
        return new BaseResponse(new MessageResponseDto("ok"));
    }

    @Override
    public BaseResponseList getUserNotifications(int userId, int offset, int limit) {
        List<Notification> notifications = personRepository.findPersonById(userId).getNotifications();
        return NotificationResponseFactory.getNotifications(notifications, offset, limit);
    }
}
