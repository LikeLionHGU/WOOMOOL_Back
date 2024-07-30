package com.project.woomool.exception;


public class GroupNameExistsException extends RuntimeException{
    private static final String MESSAGE = "이미 존재하는 그룹이름입니다. 다른 이름으로 입력해주세요";

    public GroupNameExistsException() {
        super(MESSAGE);
    }
}
