package com.vicky.blog.common.dto;

import java.time.LocalDateTime;

public class AppErrorResponse {
    
    private int status;
    private String error;
    private LocalDateTime timestamp;
    private String path = "/";
    
    public AppErrorResponse() {
        this.status = 500;
        this.error = "Internel Server Error";
        this.timestamp = LocalDateTime.now();
        this.path = "/";
    }

    public AppErrorResponse(int status, String error, LocalDateTime timestamp, String path) {
        this.status = status;
        this.error = error;
        this.timestamp = timestamp;
        this.path = path;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public String toString() {
        return "ErrorResponse [status=" + status + ", error=" + error + ", timestamp=" + timestamp + ", path=" + path
                + "]";
    }

}
