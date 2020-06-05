package com.skillbox.socialnetwork.main.service.files;

import com.skillbox.socialnetwork.main.dto.ResponseDto;
import com.skillbox.socialnetwork.main.dto.files.response.FileDto;
import com.skillbox.socialnetwork.main.model.UploadFile;
import com.skillbox.socialnetwork.main.model.enumerated.FileType;
import com.skillbox.socialnetwork.main.repository.FileRepository;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.security.jwt.JwtTokenProvider;
import com.skillbox.socialnetwork.main.service.impl.PersonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService{

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PersonServiceImpl personService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public FileDto saveImage(String token, MultipartFile file) {
        File uploadDir = new File(uploadPath + "/img");
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }

        String id = UUID.randomUUID().toString();
        int ownerId = personService.findByEmail(jwtTokenProvider.getUsername(token)).getId();
        String fileName = file.getOriginalFilename();
        String relativeFilePath = uploadPath + "/img/" + id + "." + fileName;
        String rawFileURL = uploadPath + "/img/" + id + "." + fileName;
        assert fileName != null;
        String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
        long bytes = file.getSize();
        FileType fileType = FileType.IMAGE;

        try {
            file.transferTo(new File(relativeFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        UploadFile uploadFile = new UploadFile();
        uploadFile.setId(id);
        uploadFile.setOwnerId(ownerId);
        uploadFile.setFileName(fileName);
        uploadFile.setRelativeFilePath(relativeFilePath);
        uploadFile.setRawFileURL(rawFileURL);
        uploadFile.setFileFormat(fileFormat);
        uploadFile.setBytes(bytes);
        uploadFile.setFileFormat(fileFormat);
        uploadFile.setFileType(fileType);
        uploadFile.setCreatedAt(new Date());
        fileRepository.save(uploadFile);


        return new FileDto(id, ownerId, fileName, relativeFilePath,
                rawFileURL, fileFormat, bytes, fileType, new Date());
    }
}
