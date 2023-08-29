package com.example.ap_ment.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ExceptionDetails {

    private final Integer HTTPCODE;
    private final String MESSAGE;
    private final HttpStatus HTTPSTATUS;
}
