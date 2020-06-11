package com.skillbox.socialnetwork.main.dto.universal;

import java.util.ArrayList;
import java.util.List;

public class ResponseFactory {
    public static BaseResponse getBaseResponse(Dto dto){
        return new BaseResponse(dto);
    }
    public static BaseResponseList getBaseResponseList(List<Dto> list, int offset, int limit){
        List<Dto> data = getElementsInRange(list, offset, limit);
        return new BaseResponseList(
                data.size(),//здесь не data.size(), а list.size() должно быть
                offset,
                limit,
                data
        );
    }

    public static ErrorResponse getErrorResponse(String error, String errorDescription){
        return new ErrorResponse(error, errorDescription);
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
