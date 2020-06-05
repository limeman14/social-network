package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.AbstractResponse;
import com.skillbox.socialnetwork.main.dto.profile.SearchPersonDto;
import com.skillbox.socialnetwork.main.dto.profile.WallDto;
import com.skillbox.socialnetwork.main.dto.request.AddPostRequestDto;
import com.skillbox.socialnetwork.main.dto.request.UpdateUserDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.model.Person;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
    AbstractResponse getMyProfile(Person person);

    AbstractResponse editMyProfile(Person person, UpdateUserDto request);

    BaseResponseDto deleteMyProfile(Person person);

    AbstractResponse getUserById(int id);

    WallDto getWallPosts(int id, int offset, int limit);

    AbstractResponse addPost(int id, long publishDate, AddPostRequestDto request);

    SearchPersonDto searchPeople(String name,
                                 String surname,
                                 Integer ageFrom,
                                 Integer ageTo,
                                 String country,
                                 String city,
                                 Integer offset,
                                 Integer limit);

}
