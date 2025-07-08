package com.example.webclient;

public class EmployeeServiceException extends Exception {
    public EmployeeServiceException(String errorMessage) {
        super(errorMessage);
    }
}
