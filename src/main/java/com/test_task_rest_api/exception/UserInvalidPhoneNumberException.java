package com.test_task_rest_api.exception;

public class UserInvalidPhoneNumberException extends RuntimeException{
    public UserInvalidPhoneNumberException(String message) {
        super(message);
    }
}
