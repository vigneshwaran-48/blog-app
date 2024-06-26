package com.vicky.blog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.vicky.blog.common.dto.user.UserDTO;

/**
 * 
 * In this class the property id and email both are unique. Id will be used to identify a unique user in the
 * application level. Email id will be used to contact the user.
 * 
 */
@Document
 public class User {
    
    @Id
    private String id;

    private String name;

    private int age;

    private String email;

    private String description;

    private String image;

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
        userDTO.setDescription(description);
        
        return userDTO;
    }

    public static User build(UserDTO userDTO) {

        User user = new User();
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setDescription(userDTO.getDescription());

        if(userDTO.getImage() != null) {
            user.setImage(userDTO.getImage());
        }

        return user;
    }
}
