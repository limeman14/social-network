package com.skillbox.socialnetwork.main.service;

import com.skillbox.socialnetwork.main.dto.universal.Dto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    Dto saveImage(String token, MultipartFile file) throws IOException;

    void resizeImage(File file, String dstFolder, String fileFormat) throws IOException;
}
