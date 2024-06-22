package com.vicky.blog.notificationservice.service;

import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserDTO.UserType;
import com.vicky.blog.common.dto.user.UserResponseData;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.notificationservice.client.UserServiceClient;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public String addUser(UserDTO userDTO) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUser'");
    }

    @Override
    public Optional<UserDTO> updateUser(UserDTO user) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public String deleteUser(String userId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public Optional<UserDTO> getUser(String userId) throws AppException {
        UserResponseData userResponse = userServiceClient.getUser(userId);
        if(userResponse.getStatus() != HttpStatus.SC_OK) {
            throw new AppException(userResponse.getStatus(), userResponse.getMessage());
        }
        return Optional.of(userResponse.getUser());
    }

    @Override
    public List<UserDTO> getUsers(String userId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsers'");
    }

    @Override
    public UserType getUserType(String userId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserType'");
    }

    @Override
    public void createAccount(String profileId, String description) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }
    
}
