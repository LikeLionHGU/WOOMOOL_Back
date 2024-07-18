package com.project.woomool.exception;

public class TeamCodeNotExistsException extends RuntimeException{
    private static final String MESSAGE = "팀 코드가 존재하지 않습니다.";



    public TeamCodeNotExistsException() {
        super(MESSAGE);
    }
}




