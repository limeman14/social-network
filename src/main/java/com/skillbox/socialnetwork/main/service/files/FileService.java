package com.skillbox.socialnetwork.main.service.files;

import com.skillbox.socialnetwork.main.dto.files.response.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {
    FileDto saveImage(String token, MultipartFile file) throws IOException;
    void resizeImage(File file, String dstFolder, String fileFormat) throws IOException;
}
