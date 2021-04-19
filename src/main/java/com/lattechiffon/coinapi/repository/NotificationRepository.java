package com.lattechiffon.coinapi.repository;

import com.lattechiffon.coinapi.domain.Notification;
import com.lattechiffon.coinapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndIsTokenNotExpiredTrue(User user);
    List<Notification> findByIsTokenNotExpiredTrue();
}
