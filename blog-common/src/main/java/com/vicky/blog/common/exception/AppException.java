package com.vicky.blog.common.exception;

public class AppException extends Exception {
    
    private int status;

    public AppException() {
        super("Internel Server Error");
        status = 500;
    }

    public AppException(String message) {
        super(message);
        status = 500;
    }

    public AppException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
