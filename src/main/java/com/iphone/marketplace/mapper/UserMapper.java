package com.iphone.marketplace.mapper;

import com.iphone.marketplace.dto.UserDTO;
import com.iphone.marketplace.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setTelegramId(user.getTelegramId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRegistrationDate(user.getRegistrationDate());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        
        return dto;
    }
    
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        updateEntity(user, dto);
        return user;
    }
    
    public void updateEntity(User user, UserDTO dto) {
        // Don't update id and telegramId as they are identity fields
        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        // Only update active status if it's explicitly set in DTO
        if (dto.isActive() != user.isActive()) {
            user.setActive(dto.isActive());
        }
    }
}
