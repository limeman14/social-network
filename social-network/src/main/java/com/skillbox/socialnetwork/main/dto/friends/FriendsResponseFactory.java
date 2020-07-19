package com.skillbox.socialnetwork.main.dto.friends;

import com.skillbox.socialnetwork.main.dto.universal.Dto;

public class FriendsResponseFactory {
    public static Dto getMessage(String message) {
        return new MessageDto(message);
    }
}
