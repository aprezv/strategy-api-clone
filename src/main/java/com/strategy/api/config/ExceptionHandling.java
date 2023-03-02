package com.strategy.api.config;

import com.strategy.api.base.exceptions.RestResponseException;
import com.strategy.api.logging.LogEvents;
import com.strategy.api.logging.LogKeys;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Locale;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Created on 2020-07-03.
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandling {
    private final MessageSource messageSource;
    private final String maxFileSize;

    @Autowired
    public ExceptionHandling(
            @Qualifier("exceptionsMessageSource") final MessageSource messageSource,
            @Value("${spring.servlet.multipart.max-file-size}") final String maxFileSize
            ) {
        this.messageSource = messageSource;
        this.maxFileSize = maxFileSize;
    }

    @ExceptionHandler(RestResponseException.class)
    public @ResponseBody
    ResponseEntity handleRestResponseException(HttpServletRequest request, RestResponseException ex, Locale locale) {
        String message = messageSource.getMessage(String.format("%s.message",ex.getClass().getName()),ex.getParams(), locale);
        String code = messageSource.getMessage(String.format("%s.code",ex.getClass().getName()), null, locale);

        GlobalExceptionResponse response = GlobalExceptionResponse.builder()
                .message(message)
                .timestamp(new Date())
                .path(request.getRequestURI())
                .code(code)
                .status(ex.getStatus())
                .build();

        log.error(
                message,
                kv(LogKeys.EVENT_NAME, LogEvents.REST_RESPONSE_EXCEPTION),
                kv(LogKeys.HTTP_STATUS_CODE, ex.getStatus()),
                kv(LogKeys.HTTP_REQUEST, request),
                kv(LogKeys.PATH, request.getRequestURI()),
                kv(LogKeys.EXCEPTION_CODE, code),
                kv(LogKeys.EXCEPTION_MESSAGE, message),
                kv(LogKeys.EXCEPTION_PARAMS, ex.getParams()),
                kv(LogKeys.HTTP_STATUS, HttpStatus.valueOf(ex.getStatus()).getReasonPhrase())
        );

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ResponseBody
    @ExceptionHandler({MultipartException.class, MaxUploadSizeExceededException.class, FileUploadException.class, SizeLimitExceededException.class})
    public ResponseEntity handleSizeLimitException(
            HttpServletRequest request,
            Exception ex, Locale locale
    ) {
        GlobalExceptionResponse response = GlobalExceptionResponse.builder()
                .message(messageSource.getMessage(
                        "exceptions.fileSizeExceeded",
                        new Object[]{
                                maxFileSize
                        },
                        locale))
                .timestamp(new Date())
                .path(request.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException( HttpServletRequest request,
                                           Exception ex, Locale locale) {

        String message = messageSource.getMessage("java.lang.Exception.message",null, locale);
        GlobalExceptionResponse response = GlobalExceptionResponse.builder()
                .message(message)
                .timestamp(new Date())
                .path(request.getRequestURI())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        log.error(
                message,
                kv(LogKeys.EVENT_NAME, LogEvents.EXCEPTION),
                kv(LogKeys.EXCEPTION_MESSAGE, ex.getMessage()),
                kv(LogKeys.EXCEPTION_CAUSE, ex.getCause()),
                kv(LogKeys.HTTP_REQUEST, request),
                kv(LogKeys.PATH, request.getRequestURI()),
                kv(LogKeys.HTTP_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value()),
                kv(LogKeys.EXCEPTION_MESSAGE, message),
                kv(LogKeys.HTTP_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value())
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
