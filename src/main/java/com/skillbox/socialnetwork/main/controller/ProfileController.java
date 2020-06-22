package com.skillbox.socialnetwork.main.controller;


import com.skillbox.socialnetwork.main.dto.person.request.UpdatePersonRequestDto;
import com.skillbox.socialnetwork.main.dto.post.request.AddPostRequestDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.dto.universal.Response;
import com.skillbox.socialnetwork.main.service.AuthService;
import com.skillbox.socialnetwork.main.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {
    private final AuthService authService;
    private final ProfileService profileService;

    @Autowired
    public ProfileController(AuthService authService, ProfileService profileService) {
        this.authService = authService;
        this.profileService = profileService;
    }

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<?> getMeProfile(@RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok(profileService.getMyProfile(authService.getAuthorizedUser(token)));
    }

    @PutMapping("/api/v1/users/me")
    public ResponseEntity<?> editProfile(@RequestHeader(name = "Authorization") String token,
                                         @RequestBody UpdatePersonRequestDto request) {
        return ResponseEntity.ok(profileService.editMyProfile(authService.getAuthorizedUser(token), request));
    }

    @DeleteMapping("/api/v1/users/me")
    public ResponseEntity<?> deleteProfile(@RequestHeader(name = "Authorization") String token) {
        //нужен логаут вместе с удалением хедера авторизации
        authService.logout(token);
        return ResponseEntity.ok(profileService.deleteMyProfile(authService.getAuthorizedUser(token)));
    }

    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<?> findUserById(@RequestHeader(name = "Authorization") String token, @PathVariable int id) {
        Response result = profileService.getUserById(id, authService.getAuthorizedUser(token));
        return result != null ? ResponseEntity.ok(result) : new ResponseEntity("User with id: " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/v1/users/{id}/wall")
    public ResponseEntity<?> getWallById(
            @PathVariable int id,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset,
            @RequestParam(name = "itemPerPage", defaultValue = "20", required = false) Integer limit) {
        BaseResponseList result = profileService.getWallPosts(id, offset, limit);
        return result != null ? ResponseEntity.ok(result) : new ResponseEntity("User with id: " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/v1/users/{id}/wall")
    public ResponseEntity<?> addPost(
            @PathVariable int id,
            @RequestParam(name = "publish_date", defaultValue = "0") Long publishDate,
            @RequestBody AddPostRequestDto request
    ) {
        Response result = profileService.addPost(id, publishDate, request);
        return result != null ? ResponseEntity.ok(result) : new ResponseEntity("User with id: " + id + " not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/v1/users/search")
    public ResponseEntity<?> searchProfile(
            @RequestParam(name = "first_name", required = false) String name,
            @RequestParam(name = "last_name", required = false) String surname,
            @RequestParam(name = "age_from", defaultValue = "18", required = false) Integer ageFrom,
            @RequestParam(name = "age_to", defaultValue = "200", required = false) Integer ageTo,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "offset", defaultValue = "0", required = false) Integer offset,
            @RequestParam(name = "itemPerPage", defaultValue = "20", required = false) Integer limit
    ) {
        return ResponseEntity.ok(profileService.searchPeople(name, surname, ageFrom, ageTo, country, city, offset, limit));

    }

    @PutMapping("/api/v1/users/block/{id}")
    public ResponseEntity<?> blockProfile(@RequestHeader(name = "Authorization") String token, @PathVariable int id) {
        BaseResponse result = profileService.blockUser(id, authService.getAuthorizedUser(token));
        return result != null ? ResponseEntity.ok(result) : new ResponseEntity("User with id: " + id + " not found", HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/api/v1/users/block/{id}")
    public ResponseEntity<?> unblockProfile(@RequestHeader(name = "Authorization") String token, @PathVariable int id) {
        BaseResponse result = profileService.unblockUser(id, authService.getAuthorizedUser(token));
        return result != null ? ResponseEntity.ok(result) : new ResponseEntity("User with id: " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
