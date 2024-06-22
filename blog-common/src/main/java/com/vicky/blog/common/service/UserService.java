package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserDTO.UserType;
import com.vicky.blog.common.exception.AppException;

public interface UserService {

    String addUser(UserDTO userDTO) throws AppException;
    Optional<UserDTO> updateUser(UserDTO user) throws AppException;
    String deleteUser(String userId) throws AppException;
    Optional<UserDTO> getUser(String userId) throws AppException;
    List<UserDTO> getUsers(String userId) throws AppException;
    UserType getUserType(String userId) throws AppException;
    void createAccount(String profileId, String description) throws AppException;

}
