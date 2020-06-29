package com.skillbox.socialnetwork.main.service.impl;


import com.skillbox.socialnetwork.main.dto.notifications.request.NotificationSettingRequestDto;
import com.skillbox.socialnetwork.main.dto.notifications.response.NotificationResponseFactory;
import com.skillbox.socialnetwork.main.dto.notifications.response.NotificationSettingResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.MessageResponseDto;
import com.skillbox.socialnetwork.main.model.Notification;
import com.skillbox.socialnetwork.main.model.NotificationSettings;
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

    /*
    По умолчанию данных в БД по поводу настроек оповещений нет, поэтому используем дефолтные значения false
    Иначе надо прописать чтобы при регистрации настройки оповещений выставлялись на true
     */

    @Override
    public BaseResponseList getNotificationSettings(int userId) {
        List<NotificationSettingResponseDto> settingsList = new ArrayList<>();
        NotificationSettings personSettings = notificationSettingsRepository.findByPersonId(userId);
        if (personSettings != null) {
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.POST, personSettings.isPostNotification()));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.POST_COMMENT, personSettings.isPostCommentNotification()));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.COMMENT_COMMENT, personSettings.isCommentCommentNotification()));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.FRIEND_REQUEST, personSettings.isFriendRequestNotification()));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.FRIEND_BIRTHDAY, personSettings.isFriendBirthdayNotification()));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.MESSAGE, personSettings.isMessageNotification()));
        }
        else {
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.POST, false));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.POST_COMMENT, false));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.COMMENT_COMMENT, false));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.FRIEND_REQUEST, false));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.FRIEND_BIRTHDAY, false));
            settingsList.add(new NotificationSettingResponseDto(NotificationCode.MESSAGE, false));
        }
        return new BaseResponseList(settingsList);
    }

    @Override
    public BaseResponse changeNotificationSetting(int userId, NotificationSettingRequestDto settingDto) {
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
            case "FRIEND_BIRTHDAY" : notificationSettings.setFriendBirthdayNotification(flag); break;
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
