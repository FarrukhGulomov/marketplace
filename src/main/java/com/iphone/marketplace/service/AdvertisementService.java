package com.iphone.marketplace.service;

import com.iphone.marketplace.model.Advertisement;
import java.math.BigDecimal;
import java.util.List;

public interface AdvertisementService {
    Advertisement createAdvertisement(Long userId, String title, String model, 
                                   BigDecimal price, String condition, String description);
    Advertisement updateAdvertisement(Long adId, Advertisement updatedAd);
    void deleteAdvertisement(Long adId);
    List<Advertisement> searchAdvertisements(String model, BigDecimal minPrice, BigDecimal maxPrice);
    List<Advertisement> getUserAdvertisements(Long userId);
    Advertisement approveAdvertisement(Long adId);
    Advertisement rejectAdvertisement(Long adId);
    Advertisement markAsSold(Long adId);
}
