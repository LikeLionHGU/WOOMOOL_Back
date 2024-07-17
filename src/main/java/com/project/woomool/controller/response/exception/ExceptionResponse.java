package com.project.woomool.controller.response.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ExceptionResponse {
    private String error;
    private String message;
}
