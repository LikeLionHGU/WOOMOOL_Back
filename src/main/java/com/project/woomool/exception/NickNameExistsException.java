package com.project.woomool.exception;


public class NickNameExistsException  extends RuntimeException{
    private static final String MESSAGE = "이미 존재하는 닉네임입니다. 다른 닉네임으로 입력해주세요";

    public NickNameExistsException() {
        super(MESSAGE);
    }
}
