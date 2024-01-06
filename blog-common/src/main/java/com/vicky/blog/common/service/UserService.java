package com.vicky.blog.common.service;

import java.util.List;
import java.util.Optional;

import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;

public interface UserService {

    boolean addUser(UserDTO userDTO) throws AppException;
    Optional<UserDTO> updateUser(UserDTO user) throws AppException;
    boolean deleteUser(String userId) throws AppException;
    Optional<UserDTO> getUser(String userId) throws AppException;
    List<UserDTO> getUsers(String userId) throws AppException;

}
