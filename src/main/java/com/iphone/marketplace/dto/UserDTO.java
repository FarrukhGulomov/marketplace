package com.iphone.marketplace.dto;

import com.iphone.marketplace.model.User.UserRole;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private Long telegramId;
    private String username;
    
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "First name can only contain letters, spaces, hyphens and apostrophes")
    private String firstName;
    
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "Last name can only contain letters, spaces, hyphens and apostrophes")
    private String lastName;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;
    
    private LocalDateTime registrationDate;
    private UserRole role;
    private boolean active;
    
    // Additional fields for profile management
    private int totalAdvertisements;
    private int activeAdvertisements;
    private LocalDateTime lastLoginDate;
}
