package com.vicky.blog.common.dto.user;

import java.io.Serializable;

public class UserDTO implements Serializable {

    public enum Theme {
        DARK,
        LIGHT
    }
    
    private String id;

    private String name;

    private int age;

    private String email;

    private String image;
    private Theme theme;
    private String description;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public Theme getTheme() {
        return theme;
    }
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
    public String getDescripton() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", name=" + name + ", age=" + age + ", email=" + email + ", image=" + image
                + ", theme=" + theme + "]";
    }
}
