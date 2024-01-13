package com.vicky.blog.common.dto.staticservice;

import java.time.LocalDateTime;

public class StaticResourceIdResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private Long id;

    public StaticResourceIdResponse() {}

    public StaticResourceIdResponse(int status, String message, String path, LocalDateTime time, Long id) {
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
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
