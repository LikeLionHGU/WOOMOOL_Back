package com.project.woomool.exception;

public class AlreadyJoinedException extends RuntimeException{
    private static final String MESSAGE = "해당 그룹에 이미 가입되어 있습니다.";

    public AlreadyJoinedException() {
        super(MESSAGE);
    }
}

