package com.iphone.marketplace.service;

import com.iphone.marketplace.dto.UserDTO;
import com.iphone.marketplace.model.User;
import com.iphone.marketplace.model.User.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(Long telegramId, String username);
    Optional<User> findByTelegramId(Long telegramId);
    List<UserDTO> getAllUsers();
    UserDTO updateUserProfile(Long telegramId, UserDTO userDTO);
    UserDTO updateUserPhone(Long telegramId, String phoneNumber);
    void deactivateUser(Long telegramId);
    void activateUser(Long telegramId);
    boolean isAdmin(Long telegramId);
    boolean isUserActive(Long telegramId);
    UserDTO promoteToAdmin(Long telegramId);
    UserDTO demoteFromAdmin(Long telegramId);
    void updateLastLoginDate(Long telegramId);
    UserDTO getUserProfile(Long telegramId);
    List<UserDTO> searchUsers(String query);
    List<UserDTO> getRecentlyActiveUsers(int limit);
}
