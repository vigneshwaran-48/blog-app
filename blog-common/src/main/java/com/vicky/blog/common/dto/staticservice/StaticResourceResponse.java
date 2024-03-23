package com.vicky.blog.common.dto.staticservice;

import java.time.LocalDateTime;

public class StaticResourceResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private String id;

    public StaticResourceResponse() {}

    public StaticResourceResponse(int status, String message, String path, LocalDateTime time, String id) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.time = time;
        this.id = id;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
