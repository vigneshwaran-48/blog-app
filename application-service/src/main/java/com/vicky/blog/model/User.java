package com.vicky.blog.model;

import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserDTO.Theme;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


/**
 * 
 * In this class the property id and email both are unique. Id will be used to identify a unique user in the
 * application level. But for the users emailId will be the unique identifier.
 * 
 */
@Entity
public class User {
    
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, unique = true)
    private String email;

    private String description;

    @Column(nullable = false)
    private Theme theme = Theme.LIGHT;

    @Column(nullable = false)
    private String image = "http://localhost:8083/static/resource/179rjncje984934";

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserDTO toDTO() {

        UserDTO userDTO = new UserDTO();
        userDTO.setAge(age);
        userDTO.setEmail(email);
        userDTO.setId(id);
        userDTO.setImage(image);
        userDTO.setName(name);
        userDTO.setTheme(theme);
        userDTO.setDescription(description);
        
        return userDTO;
    }

    public static User build(UserDTO userDTO) {

        User user = new User();
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setDescription(userDTO.getDescripton());

        if(userDTO.getTheme() != null) {
            user.setTheme(userDTO.getTheme());
        }

        if(userDTO.getImage() != null) {
            user.setImage(userDTO.getImage());
        }

        return user;
    }
}
