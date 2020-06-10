package com.skillbox.socialnetwork.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationsController {

    @GetMapping
    public ResponseEntity<?> getNotification(
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "20") Integer itemPerPage
    ){

        return ResponseEntity.status(HttpStatus.OK).body("true");
    }
}
