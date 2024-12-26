package com.iphone.marketplace.service.impl;

import com.iphone.marketplace.model.Advertisement;
import com.iphone.marketplace.model.Advertisement.AdvertisementStatus;
import com.iphone.marketplace.model.User;
import com.iphone.marketplace.repository.AdvertisementRepository;
import com.iphone.marketplace.repository.UserRepository;
import com.iphone.marketplace.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Advertisement createAdvertisement(Long userId, String title, String model,
                                          BigDecimal price, String condition, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Advertisement ad = new Advertisement();
        ad.setUser(user);
        ad.setTitle(title);
        ad.setModel(model);
        ad.setPrice(price);
        ad.setCondition(condition);
        ad.setDescription(description);

        return advertisementRepository.save(ad);
    }

    @Override
    @Transactional
    public Advertisement updateAdvertisement(Long adId, Advertisement updatedAd) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));

        ad.setTitle(updatedAd.getTitle());
        ad.setModel(updatedAd.getModel());
        ad.setPrice(updatedAd.getPrice());
        ad.setCondition(updatedAd.getCondition());
        ad.setDescription(updatedAd.getDescription());

        return advertisementRepository.save(ad);
    }

    @Override
    @Transactional
    public void deleteAdvertisement(Long adId) {
        advertisementRepository.deleteById(adId);
    }

    @Override
    public List<Advertisement> searchAdvertisements(String model, BigDecimal minPrice, BigDecimal maxPrice) {
        return advertisementRepository.findByModelContainingIgnoreCase(model).stream()
                .filter(ad -> ad.getStatus() == AdvertisementStatus.APPROVED)
                .filter(ad -> ad.getPrice().compareTo(minPrice) >= 0 && 
                            ad.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Advertisement> getUserAdvertisements(Long userId) {
        return advertisementRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Advertisement approveAdvertisement(Long adId) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        ad.setStatus(AdvertisementStatus.APPROVED);
        return advertisementRepository.save(ad);
    }

    @Override
    @Transactional
    public Advertisement rejectAdvertisement(Long adId) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        ad.setStatus(AdvertisementStatus.REJECTED);
        return advertisementRepository.save(ad);
    }

    @Override
    @Transactional
    public Advertisement markAsSold(Long adId) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        ad.setStatus(AdvertisementStatus.SOLD);
        return advertisementRepository.save(ad);
    }
}
