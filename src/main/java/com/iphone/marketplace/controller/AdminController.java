package com.iphone.marketplace.controller;

import com.iphone.marketplace.model.Advertisement;
import com.iphone.marketplace.dto.UserDTO;
import com.iphone.marketplace.service.AdvertisementService;
import com.iphone.marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AdvertisementService advertisementService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/users/{telegramId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long telegramId) {
        userService.deactivateUser(telegramId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/advertisements/{adId}/approve")
    public ResponseEntity<Advertisement> approveAdvertisement(@PathVariable Long adId) {
        return ResponseEntity.ok(advertisementService.approveAdvertisement(adId));
    }

    @PostMapping("/advertisements/{adId}/reject")
    public ResponseEntity<Advertisement> rejectAdvertisement(@PathVariable Long adId) {
        return ResponseEntity.ok(advertisementService.rejectAdvertisement(adId));
    }

    @DeleteMapping("/advertisements/{adId}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long adId) {
        advertisementService.deleteAdvertisement(adId);
        return ResponseEntity.ok().build();
    }
}
