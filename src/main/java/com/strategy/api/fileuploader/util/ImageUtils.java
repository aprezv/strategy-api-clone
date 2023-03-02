package com.strategy.api.fileuploader.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 2021-12-07.
 */
public class ImageUtils {
    public static boolean isImage(MultipartFile multipartFile) {
        try {
            return isImage(multipartFile.getInputStream());
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isImage(InputStream inputStream){
        try {
            ImageIO.read(inputStream).toString();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
