package com.iphone.marketplace.repository;

import com.iphone.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(Long telegramId);
    boolean existsByTelegramId(Long telegramId);
    List<User> findByUsernameContainingOrFirstNameContainingOrLastNameContaining(String username, String firstName, String lastName);
    Page<User> findByOrderByLastLoginDateDesc(Pageable pageable);
}
