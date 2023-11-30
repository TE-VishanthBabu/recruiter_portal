package com.job_portal.recruiter_portal.utils;

import com.job_portal.recruiter_portal.constants.Constant;
import com.job_portal.recruiter_portal.customException.InvalidFileFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class ValidationUtils {

    public void validateUploadPhoto(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName != null && (!(fileName.toLowerCase().endsWith(Constant.JPEG) || fileName.toLowerCase().endsWith(Constant.JPG)
                || fileName.toLowerCase().endsWith(Constant.GIF) || fileName.toLowerCase().endsWith(Constant.PNG) ||
                fileName.toLowerCase().endsWith("jfif")))) {
            log.error("Invalid Image format");
            throw new InvalidFileFormat("photo upload failed");
        }
    }

    public void validateUploadPdf(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName != null && !(fileName.toLowerCase().endsWith(Constant.PDF))){
            log.error("Invalid File format. Please upload pdf file");
            throw new InvalidFileFormat("Please upload pdf file");
        }
    }
}
