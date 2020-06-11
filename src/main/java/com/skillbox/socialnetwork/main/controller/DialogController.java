package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.universal.BaseResponseList;
import com.skillbox.socialnetwork.main.service.AuthService;
import com.skillbox.socialnetwork.main.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dialogs")
public class DialogController {

    private final DialogService dialogService;
    private final AuthService authService;

    @Autowired
    public DialogController(DialogService dialogService, AuthService authService) {
        this.dialogService = dialogService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<BaseResponseList> getAllMessages(
            @RequestParam(name = "query", defaultValue = "", required = false) String query,
            @RequestParam(name = "offset", defaultValue = "0", required = false) Integer offset,
            @RequestParam(name = "itemPerPage", defaultValue = "20", required = false) Integer limit,
            @RequestHeader(name = "Authorization") String token
    ){
        return ResponseEntity.ok(dialogService.getAllMessages(query, offset, limit, authService.getAuthorizedUser(token)));
    }
}
