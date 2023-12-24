package com.vicky.blog.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserResponse;
import com.vicky.blog.common.dto.user.UserResponseData;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/app/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * Need to remove this controller later because user should be created by the login process only.
     */
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO user, HttpServletRequest request) 
        throws AppException {

        boolean userAdded = userService.addUser(user);

        if(!userAdded) {
            throw new AppException(500, "Error while creating user");
        }

        Optional<UserDTO> userDTO = userService.getUser(user.getId());

        UserResponseData response = new UserResponseData();
        response.setStatus(HttpStatus.SC_CREATED);
        response.setMessage("Created user");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setUser(userDTO.get());

        return ResponseEntity.status(HttpStatus.SC_CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) 
        throws AppException {

        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        UserResponseData response = new UserResponseData();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("Updated user");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setUser(updatedUser.get());

        return ResponseEntity.status(HttpStatus.SC_OK).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String userId, HttpServletRequest request) 
        throws AppException {

        UserDTO user = userService.getUser(userId).orElseThrow(() -> 
                                        new AppException(HttpStatus.SC_BAD_REQUEST, "User not exists"));

        UserResponseData response = new UserResponseData();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setUser(user);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId, HttpServletRequest request) 
        throws AppException {

        boolean deleted = userService.deleteUser(userId);

        if(!deleted) {
            throw new AppException("Unable to delete the user");
        }
        UserResponse response = new UserResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }
}
