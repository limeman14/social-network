package com.skillbox.socialnetwork.main.dto.dialog;

import com.skillbox.socialnetwork.main.dto.dialog.response.MessageDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogFactory {
    public static BaseResponseList getMessages(List<Message> messageList, int limit, int offset){
        return new BaseResponseList(
                messageList.size(),
                offset,
                limit,
                formatMessages(messageList, limit, offset)
        );
    }

    private static List<Dto> formatMessages(List<Message> messages, int limit, int offset){
        return getElementsInRange(messages
                .stream()
                .map(message -> new MessageDto(
                        message.getId(),
                        message.getTime().getTime(),
                        message.getAuthor().getId(),
                        message.getRecipient().getId(),
                        message.getMessageText(),
                        message.getReadStatus()))
                .collect(Collectors.toList()),
                limit, offset);
    }

    private static List<Dto> getElementsInRange(List<Dto> list, int offset, int limit) {
        int lastElementIndex = offset + limit;
        int lastPostIndex = list.size();
        if (lastPostIndex >= offset) {//если есть элементы входящие в нужный диапазон
            if (lastElementIndex <= lastPostIndex) {//если все элементы с нужными индексами есть в листе
                return list.subList(offset, lastElementIndex);
            } else {//если не хватает элементов, то в посты записываем остаток, считая от offset
                return list.subList(offset, lastPostIndex);
            }
        } else {
            return new ArrayList<>();
        }
    }
}
