package com.vicky.blog.common.dto.user;

import java.time.LocalDateTime;

public class UserResponseData {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private UserDTO user;

    public UserResponseData() {}

    public UserResponseData(int status, String message, String path, LocalDateTime time, UserDTO user) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.time = time;
        this.user = user;
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
    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }

}
