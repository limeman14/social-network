package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.files.request.FileRequestDto;
import com.skillbox.socialnetwork.main.dto.files.response.FileResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.Dto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseFactory;
import com.skillbox.socialnetwork.main.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<?> saveFile(@RequestHeader(name = "Authorization") String token, FileRequestDto fileRequest) throws IOException {
        if (fileRequest.getFile() != null) {
            Dto dto;
            switch (fileRequest.getType()) {
                case "IMAGE": {
                    dto = fileService.saveImage(token, fileRequest.getFile());
                    break;
                }
                default:
                    dto = new FileResponseDto();
            }
            return ResponseEntity.ok(ResponseFactory.getBaseResponse(dto));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
