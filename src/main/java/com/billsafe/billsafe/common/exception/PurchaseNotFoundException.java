package com.billsafe.billsafe.common.exception;

public class PurchaseNotFoundException extends RuntimeException{
    public PurchaseNotFoundException(String message) {
        super(message);
    }
}
