package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.notifications.request.NotificationSettingRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;

public interface NotificationService {
    BaseResponseList getNotificationSettings(int userId);

    BaseResponse changeNotificationSetting(int userId, NotificationSettingRequestDto settingDto);

    BaseResponseList getUserNotifications(int userId, int offset, int limit);



}
