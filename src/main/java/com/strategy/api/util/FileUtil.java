package com.strategy.api.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created on 5/26/22.
 */
@Slf4j
public class FileUtil {

    public static String loadFileContent(String filePath) {

        String content = "";

        try {
            Resource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getInputStream();

            content = IOUtils
                    .toString(inputStream, StandardCharsets.UTF_8.toString());
        } catch (IOException e) {
            log.error("Could not load file content {}. Exception is: {}", filePath, e.getMessage());
        }

        return content;
    }
}
