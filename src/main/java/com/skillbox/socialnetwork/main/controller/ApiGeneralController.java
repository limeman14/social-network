package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.files.response.FileDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.dto.universal.ResponseDto;
import com.skillbox.socialnetwork.main.service.files.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
public class ApiGeneralController {
    private final FileServiceImpl fileService;

    @Autowired
    public ApiGeneralController(FileServiceImpl fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/storage")
    public ResponseEntity<?> saveFile(@RequestHeader(name = "Authorization") String token, String type, MultipartFile file) throws IOException {
        if (file != null){
            ResponseDto response;
            switch (type){
                case "IMAGE": {
                    response = fileService.saveImage(token, file);
                    break;
                }
                default: response = new FileDto();
            }
            return ResponseEntity.ok(new BaseResponseDto("", new Date().getTime(), response));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
