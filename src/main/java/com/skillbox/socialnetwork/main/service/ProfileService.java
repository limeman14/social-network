package com.skillbox.socialnetwork.main.service;


import com.skillbox.socialnetwork.main.dto.person.request.UpdatePersonRequestDto;
import com.skillbox.socialnetwork.main.dto.profile.SearchPersonDto;
import com.skillbox.socialnetwork.main.dto.profile.WallDto;
import com.skillbox.socialnetwork.main.dto.request.AddPostRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.model.Person;

public interface ProfileService {
    ResponseDto getMyProfile(Person person);

    ResponseDto editMyProfile(Person person, UpdatePersonRequestDto request);

    BaseResponseDto deleteMyProfile(Person person);

    ResponseDto getUserById(int id, Person authorizedUser);

    WallDto getWallPosts(int id, int offset, int limit);

    ResponseDto addPost(int id, long publishDate, AddPostRequestDto request);

    SearchPersonDto searchPeople(String name,
                                 String surname,
                                 Integer ageFrom,
                                 Integer ageTo,
                                 String country,
                                 String city,
                                 Integer offset,
                                 Integer limit,
                                 Person auhtorizedUser);
    BaseResponseDto blockUser(int idOfABlockedUser, Person authorizedUser);

    BaseResponseDto unblockUser(int id, Person authorizedUser);
}
