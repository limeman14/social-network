package com.skillbox.socialnetwork.main.service.files;

import com.skillbox.socialnetwork.main.dto.files.response.FileDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDto saveImage(String token, MultipartFile file);
}
