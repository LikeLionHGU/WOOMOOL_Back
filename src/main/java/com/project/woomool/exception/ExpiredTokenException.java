package com.project.woomool.exception;

public class ExpiredTokenException extends RuntimeException{
    private static final String MESSAGE = "토큰이 만료되었습니다.";



    public ExpiredTokenException() {
        super(MESSAGE);
    }
}
