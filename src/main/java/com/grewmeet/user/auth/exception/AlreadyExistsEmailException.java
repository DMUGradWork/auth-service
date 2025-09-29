package com.grewmeet.user.auth.exception;

public class AlreadyExistsEmailException extends RuntimeException {

    private static final String ERROR_MESSAGE = "%s - 이미 가입된 이메일 입니다.";

    public AlreadyExistsEmailException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
