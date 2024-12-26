package com.iphone.marketplace.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long telegramId;

    @Column(nullable = false)
    private String username;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "First name can only contain letters, spaces, hyphens and apostrophes")
    @Column(length = 50)
    private String firstName;

    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "Last name can only contain letters, spaces, hyphens and apostrophes")
    @Column(length = 50)
    private String lastName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column
    private LocalDateTime lastLoginDate;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    private boolean isActive = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Advertisement> advertisements = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
        lastLoginDate = LocalDateTime.now();
    }

    public String getFullName() {
        if (firstName == null && lastName == null) {
            return username;
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }

    public void updateLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now();
    }

    public int getTotalAdvertisements() {
        return advertisements.size();
    }

    public int getActiveAdvertisements() {
        return (int) advertisements.stream()
                .filter(ad -> ad.getStatus() == Advertisement.AdvertisementStatus.APPROVED)
                .count();
    }

    public enum UserRole {
        USER,
        ADMIN
    }
}
