package com.vicky.blog.common.utility;

public abstract class Notifier {
        
    protected Object[] data;

    public void setData(Object... data) {
        this.data = data;
    }

    public abstract void execute(String accessToken);
}