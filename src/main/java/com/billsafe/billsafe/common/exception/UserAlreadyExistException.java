package com.billsafe.billsafe.common.exception;

public class UserAlreadyExistException extends RuntimeException
{
    public UserAlreadyExistException(String message)
    {
        super(message);
    }
}
