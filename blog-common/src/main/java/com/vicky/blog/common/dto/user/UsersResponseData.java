package com.vicky.blog.common.dto.user;

import java.time.LocalDateTime;
import java.util.List;

public class UsersResponseData {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private List<UserDTO> users;

    public UsersResponseData() {}

    public UsersResponseData(int status, String message, String path, LocalDateTime time, List<UserDTO> users) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.time = time;
        this.users = users;
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
    public List<UserDTO> getUsers() {
        return users;
    }
    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

}
