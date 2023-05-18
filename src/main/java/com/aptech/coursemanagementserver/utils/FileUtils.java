package com.aptech.coursemanagementserver.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
    public static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
            extension = originalFilename.substring(dotIndex + 1);
        }

        return extension;
    }

    public static String getFileExtension(String fileName) {
        String extension = "";

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex + 1);
        }

        return extension;
    }
}
