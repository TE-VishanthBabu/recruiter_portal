package com.job_portal.recruiter_portal.service;

import com.job_portal.recruiter_portal.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {
    private final ValidationUtils validationUtils;
    private final FileStorageService storageService;

    /**
     * Method to upload image.
     *
     * @param file  Multipart file
     * @return Filename of uploaded image
     */
    public String uploadImage(MultipartFile file) {
        if(ObjectUtils.isNotEmpty(file)){
            this.validationUtils.validateUploadPhoto(file);
            Integer index = file.getOriginalFilename().indexOf(".");
            String fileName = file.getOriginalFilename().substring(0,index);
            fileName = fileName.replaceAll(" ","_");
            return storageService.storeFile(file,  new Timestamp(System.currentTimeMillis()).getTime() + "__"+fileName);
        } else {
            return null;
        }
    }

    /**
     * Method to upload pdf.
     *
     * @param file  Multipart file
     * @return Filename of uploaded pdf
     */
    public String uploadPdf(MultipartFile file) {
        if (ObjectUtils.isNotEmpty(file)) {
            this.validationUtils.validateUploadPdf(file);
            Integer index = file.getOriginalFilename().indexOf(".");
            String fileName = file.getOriginalFilename().substring(0,index);
            fileName = fileName.replaceAll(" ","_");
            return storageService.storeFile(file,  new Timestamp(System.currentTimeMillis()).getTime() + "__"+fileName);
        } else {
            return null;
        }
    }
}
