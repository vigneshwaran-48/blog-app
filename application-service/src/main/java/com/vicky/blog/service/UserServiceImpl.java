package com.vicky.blog.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean addUser(UserDTO userDTO) throws AppException {

        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            LOGGER.error("User with email id {} already exists", userDTO.getEmail());
            throw new AppException(409, "User with email id already exists");
        }
        User user = User.build(userDTO);
        User addedUser = userRepository.save(user);

        if(addedUser != null) {
            LOGGER.info("Added user {}", addedUser.getId());
            return true;
        }
        return false;
    }

    @Override
    public Optional<UserDTO> updateUser(UserDTO user) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public boolean deleteUser(String userId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public Optional<UserDTO> getUser(String userId) throws AppException {
        
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(user.get().toDTO());
    }
    
}
