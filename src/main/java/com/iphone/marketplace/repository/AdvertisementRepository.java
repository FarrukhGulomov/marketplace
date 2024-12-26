package com.iphone.marketplace.repository;

import com.iphone.marketplace.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByUserId(Long userId);
    List<Advertisement> findByModelContainingIgnoreCase(String model);
    List<Advertisement> findByStatus(String status);
}
