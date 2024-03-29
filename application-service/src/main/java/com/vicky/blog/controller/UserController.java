package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.EmptyResponse;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserResponseData;
import com.vicky.blog.common.dto.user.UsersResponseData;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/app/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserIdExtracter userIdExtracter;

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

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request) 
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

    @GetMapping("/profile")
    public ResponseEntity<?> getUser(HttpServletRequest request, Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);

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

    @GetMapping
    public ResponseEntity<?> getUsers(HttpServletRequest request, Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        List<UserDTO> users = userService.getUsers(userId);

        UsersResponseData response = new UsersResponseData();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setUsers(users);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId, HttpServletRequest request) 
        throws AppException {

        boolean deleted = userService.deleteUser(userId);

        if(!deleted) {
            throw new AppException("Unable to delete the user");
        }
        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }
}
