package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.files.request.FileRequestDto;
import com.skillbox.socialnetwork.main.dto.files.response.FileResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.security.jwt.JwtUser;
import com.skillbox.socialnetwork.main.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class ApiGeneralController {
    private final FileServiceImpl fileService;

    @Autowired
    public ApiGeneralController(FileServiceImpl fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/storage")
    public ResponseEntity<?> saveFile(@AuthenticationPrincipal JwtUser user,
                                      FileRequestDto fileRequest) throws IOException {
        return ResponseEntity.ok(fileService.saveFile(user.getEmail(), fileRequest));
    }
}
