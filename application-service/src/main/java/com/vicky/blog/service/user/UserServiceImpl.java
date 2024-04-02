package com.vicky.blog.service.user;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.profile.ProfileDTO.ProfileType;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.dto.user.UserDTO.UserType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.ProfileIdService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.model.User;
import com.vicky.blog.repository.mongo.UserMongoRepository;
import com.vicky.blog.service.I18NMessages;
import com.vicky.blog.service.I18NMessages.I18NMessage;
import com.vicky.blog.service.profileId.ProfileIdUtil;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMongoRepository userRepository;

    @Autowired
    private I18NMessages i18nMessages;

    @Autowired
    private ProfileIdService profileIdService;

    @Autowired
    private ProfileIdUtil profileIdUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean addUser(UserDTO userDTO) throws AppException {

        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            LOGGER.error("User with email id {} already exists", userDTO.getEmail());
            throw new AppException(HttpStatus.SC_CONFLICT, "User with email id already exists");
        }
        validateUser(userDTO);
        User user = User.build(userDTO);
        User addedUser = userRepository.save(user);

        if(addedUser != null) {
            LOGGER.info("Added user {}", addedUser.getId());
            String profileId = userDTO.getProfileId();
            if(profileId == null) {
                profileId = addedUser.getId();
            }
            profileIdService.addProfileId(addedUser.getId(), profileId, ProfileType.USER);
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
        validateUser(user);
        checkAndFillMissingDataForPatchUpdate(user, existingUser.get());
        User updatedUser = userRepository.save(User.build(user));
        if(updatedUser == null) {
            LOGGER.error("Error while updating user {}", user.getId());
            throw new AppException("Error while updating user");
        }
        profileIdService.updateProfileId(updatedUser.getId(), user.getProfileId(), ProfileType.USER);
        UserDTO updateUserDTO = updatedUser.toDTO();
        Optional<String> profileId = profileIdService.getProfileIdByEntityId(updateUserDTO.getId());
        updateUserDTO.setProfileId(profileId.isPresent() ? profileId.get() : updateUserDTO.getId());
        return Optional.of(updateUserDTO);
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
        UserDTO userDTO = user.get().toDTO();
        Optional<String> profileId = profileIdService.getProfileIdByEntityId(userDTO.getId());
        userDTO.setProfileId(profileId.isPresent() ? profileId.get() : userDTO.getId());
        return Optional.of(userDTO);
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
                                    UserDTO userDTO = user.toDTO();
                                    try {
                                        Optional<String> profileId = profileIdService.getProfileIdByEntityId(userDTO.getId());
                                        userDTO.setProfileId(profileId.isPresent() ? profileId.get() : userDTO.getId());
                                    } catch (AppException e) {
                                        LOGGER.error(e.getMessage(), e);
                                    }
                                    return userDTO;
                                }).collect(Collectors.toList());
    }

    @Override
    public UserType getUserType(String userId) throws AppException {
        if (userId == null) {
            return UserType.GUEST;
        } else {
            // Need to add logic for premium users.
            return UserType.NORMAL;
        }
    }


    private void checkAndFillMissingDataForPatchUpdate(UserDTO newData, User existingData) throws AppException {
        if(newData.getAge() == 0) {
            newData.setAge(existingData.getAge());
        }
        if(newData.getName() == null) {
            newData.setName(existingData.getName());
        }
        if(newData.getDescription() == null) {
            newData.setDescription(existingData.getDescription());
        }
        if(newData.getEmail() == null) {
            newData.setEmail(existingData.getEmail());
        }
        else { 
            Optional<User> userWithEmail = userRepository.findByEmail(newData.getEmail());
            if(userWithEmail.isPresent() && !userWithEmail.get().getId().equals(newData.getId())) {
                LOGGER.error("User with email id {} already exists", newData.getEmail());
                throw new AppException(HttpStatus.SC_CONFLICT, "User with email id already exists");
            }
        }
        if(newData.getImage() == null) {
            newData.setImage(existingData.getImage());
        }
        if(newData.getTheme() == null) {
            newData.setTheme(existingData.getTheme());
        }
    }
    
    private void validateUser(UserDTO userDTO) throws AppException {
        profileIdUtil.validateUniqueName(userDTO.getId(), userDTO.getProfileId());
        validateName(userDTO.getName());
        validateAge(userDTO.getAge());
        validateDescription(userDTO.getDescription());
        validateEmail(userDTO.getEmail());
    }

    private void validateName(String name) throws AppException {
        if(name == null) {
            Object[] args = { "Name" };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, args));
        }
        if(name.length() < UserConstants.MIN_LENGTH || name.length() > UserConstants.MAX_LENGTH) {
            Object[] args = { "User name", UserConstants.MIN_LENGTH, UserConstants.MAX_LENGTH };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.MIN_MAX, args));
        }
    }

    private void validateAge(int age) throws AppException {
        if(age < UserConstants.MIN_AGE || age > UserConstants.MAX_AGE) {
            Object[] args = { "User age", UserConstants.MIN_AGE, UserConstants.MAX_AGE };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.MIN_MAX, args));
        }
    }

    private void validateDescription(String description) throws AppException {
        if(description != null && description.length() > UserConstants.MAX_DESCRIPTION_LENGTH) {
            Object[] args = { "Description", UserConstants.MAX_DESCRIPTION_LENGTH };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.MAX_LENGTH, args));
        }
    }

    private void validateEmail(String email) throws AppException {
        if(email == null) {
            Object[] args = { "Email" };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.REQUIRED, args));
        }
        if(!Pattern.compile(UserConstants.EMAIL_REGEX).matcher(email).matches()) {
            Object[] args = { "Email" };
            throw new AppException(HttpStatus.SC_BAD_REQUEST, i18nMessages.getMessage(I18NMessage.INVALID, args));
        }
    }
    

}
