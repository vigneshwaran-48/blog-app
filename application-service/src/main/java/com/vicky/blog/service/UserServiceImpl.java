package com.vicky.blog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
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
            throw new AppException(HttpStatus.SC_CONFLICT, "User with email id already exists");
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
        Optional<User> existingUser = userRepository.findById(user.getId());

        if(existingUser.isEmpty()) {
            LOGGER.error("User with userId {} not found", user.getId());
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "User not exists");
        }

        if(user.getAge() == 0) {
            user.setAge(existingUser.get().getAge());
        }
        if(user.getName() == null) {
            user.setName(existingUser.get().getName());
        }
        if(user.getDescripton() == null) {
            user.setDescription(existingUser.get().getDescription());
        }
        if(user.getEmail() == null) {
            user.setEmail(existingUser.get().getEmail());
        }
        else { 
            Optional<User> userWithEmail = userRepository.findByEmail(user.getEmail());
            if(userWithEmail.isPresent() && !userWithEmail.get().getId().equals(user.getId())) {
                LOGGER.error("User with email id {} already exists", user.getEmail());
                throw new AppException(HttpStatus.SC_CONFLICT, "User with email id already exists");
            }
        }
        if(user.getImage() == null) {
            user.setImage(existingUser.get().getImage());
        }
        if(user.getTheme() == null) {
            user.setTheme(existingUser.get().getTheme());
        }

        User updatedUser = userRepository.save(User.build(user));
        if(updatedUser == null) {
            LOGGER.error("Error while updating user {}", user.getId());
            throw new AppException("Error while updating user");
        }
        return Optional.of(updatedUser.toDTO());
    }

    @Override
    public boolean deleteUser(String userId) throws AppException {
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    public Optional<UserDTO> getUser(String userId) throws AppException {
        
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(user.get().toDTO());
    }

    @Override
    public List<UserDTO> getUsers(String userId) throws AppException {
        if(getUser(userId).isEmpty()) {
            LOGGER.error("User {} is not registered", userId);
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Current session user is not registered");
        }

        List<User> users = userRepository.findAll();
        if(users.isEmpty()) {
            return List.of();
        }
        return users.stream().map(user -> {
                                    return user.toDTO();
                                }).collect(Collectors.toList());
    }
    
}
