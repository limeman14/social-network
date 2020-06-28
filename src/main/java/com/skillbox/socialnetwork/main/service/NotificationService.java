package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.notifications.request.NotificationSettingDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;

public interface NotificationService {
    BaseResponseList getNotificationSettings(int userId);

    BaseResponse changeNotificationSetting(int userId, NotificationSettingDto settingDto);

    BaseResponseList getUserNotifications(int userId, int offset, int limit);



}
