package com.iphone.marketplace.service.impl;

import com.iphone.marketplace.dto.UserDTO;
import com.iphone.marketplace.exception.UserAlreadyExistsException;
import com.iphone.marketplace.exception.UserNotFoundException;
import com.iphone.marketplace.mapper.UserMapper;
import com.iphone.marketplace.model.User;
import com.iphone.marketplace.model.User.UserRole;
import com.iphone.marketplace.repository.UserRepository;
import com.iphone.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User registerUser(Long telegramId, String username) {
        log.info("Attempting to register user with telegramId: {}", telegramId);
        if (userRepository.existsByTelegramId(telegramId)) {
            log.warn("User with telegramId {} already exists", telegramId);
            throw new UserAlreadyExistsException("User with telegram ID " + telegramId + " already exists");
        }

        User user = new User();
        user.setTelegramId(telegramId);
        user.setUsername(username);
        user.setRole(UserRole.USER);
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        log.info("Successfully registered user with telegramId: {}", telegramId);
        return savedUser;
    }

    @Override
    public Optional<User> findByTelegramId(Long telegramId) {
        log.debug("Finding user by telegramId: {}", telegramId);
        return userRepository.findByTelegramId(telegramId);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateUserProfile(Long telegramId, UserDTO userDTO) {
        log.info("Updating profile for user with telegramId: {}", telegramId);
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new UserNotFoundException("User not found with telegram ID: " + telegramId));
        
        userMapper.updateEntity(user, userDTO);
        User savedUser = userRepository.save(user);
        log.info("Successfully updated profile for user with telegramId: {}", telegramId);
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUserPhone(Long telegramId, String phoneNumber) {
        log.info("Updating phone number for user with telegramId: {}", telegramId);
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new UserNotFoundException("User not found with telegram ID: " + telegramId));
        
        user.setPhoneNumber(phoneNumber);
        User savedUser = userRepository.save(user);
        log.info("Successfully updated phone number for user with telegramId: {}", telegramId);
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public void deactivateUser(Long telegramId) {
        log.info("Deactivating user with telegramId: {}", telegramId);
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new UserNotFoundException("User not found with telegram ID: " + telegramId));
        user.setActive(false);
        userRepository.save(user);
        log.info("Successfully deactivated user with telegramId: {}", telegramId);
    }

    @Override
    @Transactional
    public void activateUser(Long telegramId) {
        log.info("Activating user with telegramId: {}", telegramId);
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new UserNotFoundException("User not found with telegram ID: " + telegramId));
        user.setActive(true);
        userRepository.save(user);
        log.info("Successfully activated user with telegramId: {}", telegramId);
    }

    @Override
    public boolean isAdmin(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .<Boolean>map(user -> user.getRole() == UserRole.ADMIN)
                .orElse(false);
    }

    @Override
    public boolean isUserActive(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .map(User::isActive)
                .orElse(false);
    }

    @Override
    @Transactional
    public UserDTO promoteToAdmin(Long telegramId) {
        log.info("Promoting user to admin with telegramId: {}", telegramId);
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new UserNotFoundException("User not found with telegram ID: " + telegramId));
        user.setRole(UserRole.ADMIN);
        User savedUser = userRepository.save(user);
        log.info("Successfully promoted user to admin with telegramId: {}", telegramId);
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO demoteFromAdmin(Long telegramId) {
        log.info("Demoting user from admin with telegramId: {}", telegramId);
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new UserNotFoundException("User not found with telegram ID: " + telegramId));
        user.setRole(UserRole.USER);
        User savedUser = userRepository.save(user);
        log.info("Successfully demoted user from admin with telegramId: {}", telegramId);
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public void updateLastLoginDate(Long telegramId) {
        log.debug("Updating last login date for user with telegramId: {}", telegramId);
        userRepository.findByTelegramId(telegramId).ifPresent(user -> {
            user.updateLastLoginDate();
            userRepository.save(user);
        });
    }

    @Override
    public UserDTO getUserProfile(Long telegramId) {
        log.debug("Fetching user profile for telegramId: {}", telegramId);
        return userRepository.findByTelegramId(telegramId)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found with telegram ID: " + telegramId));
    }

    @Override
    public List<UserDTO> searchUsers(String query) {
        log.debug("Searching users with query: {}", query);
        return userRepository.findByUsernameContainingOrFirstNameContainingOrLastNameContaining(
                query, query, query).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getRecentlyActiveUsers(int limit) {
        log.debug("Fetching {} recently active users", limit);
        return userRepository.findByOrderByLastLoginDateDesc(PageRequest.of(0, limit)).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
