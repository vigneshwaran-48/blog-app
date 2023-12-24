package com.vicky.blog.common.dto.user;

import java.time.LocalDateTime;

public class UserResponse {

    private int status;
    private String message;
    private String path;
    private LocalDateTime time;

    public UserResponse() {}

    public UserResponse(int status, String message, String path, LocalDateTime time) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.time = time;
    }
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
