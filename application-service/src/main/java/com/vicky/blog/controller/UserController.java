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
import com.vicky.blog.common.dto.preference.PreferenceDTO;
import com.vicky.blog.common.dto.preference.PreferenceResponse;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserResponseData;
import com.vicky.blog.common.dto.user.UsersResponseData;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.FollowService;
import com.vicky.blog.common.service.PreferenceService;
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

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private FollowService followService;

    /**
     * Need to remove this controller later because user should be created by the login process only.
     */
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDTO user, HttpServletRequest request) throws AppException {

        String userId = userService.addUser(user);

        if (userId == null) {
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
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request) throws AppException {

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

        UserDTO user = userService.getUser(userId)
                .orElseThrow(() -> new AppException(HttpStatus.SC_BAD_REQUEST, "User not exists"));

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

        UserDTO user = userService.getUser(userId).orElse(null);

        int status = HttpStatus.SC_OK;

        if (user == null) {
            user = new UserDTO();
            user.setAge(-1);
            user.setName("Guest");
            user.setId(userId);
            user.setProfileId(userId);
            user.setPreferences(preferenceService.getDefaultPreferences());
            status = 401;
        }

        UserResponseData response = new UserResponseData();
        response.setStatus(status);
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

        userService.deleteUser(userId);

        EmptyResponse response = new EmptyResponse();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/preferences")
    public ResponseEntity<PreferenceResponse> updatePreferences(@RequestBody PreferenceDTO preferenceDTO,
            HttpServletRequest request, Principal principal) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        preferenceDTO.setUserId(userId);
        PreferenceDTO preferences = preferenceService.updatePreferences(userId, preferenceDTO);
        PreferenceResponse response = new PreferenceResponse();
        response.setMessage("sucess");
        response.setPath(request.getServletPath());
        response.setPreferences(preferences);
        response.setStatus(HttpStatus.SC_OK);
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/most-followed")
    public ResponseEntity<UsersResponseData> getMostFollowedUsers(Principal principal, HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);

        List<UserDTO> mostFollowedUsers = followService.getMostFollowedUsers(userId);
        UsersResponseData response = new UsersResponseData();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setUsers(mostFollowedUsers);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/following")
    public ResponseEntity<UsersResponseData> getFollowingUsers(Principal principal, HttpServletRequest request) throws AppException {
        String userId = userIdExtracter.getUserId(principal);

        List<UserDTO> followingUsers = followService.getFollowingUsers(userId);
        UsersResponseData response = new UsersResponseData();
        response.setStatus(HttpStatus.SC_OK);
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setUsers(followingUsers);

        return ResponseEntity.ok().body(response);
    }

   public ResponseEntity<EmptyResponse> createAccount(@RequestParam String profileId, @RequestParam String description,
       Principal principal, HttpServletRequest request) throws AppException {

       String userId = userIdExtracter.getUserId(principal);
       userService.createAccount();
   }
}
