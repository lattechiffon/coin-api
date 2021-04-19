package com.lattechiffon.coinapi.service;

import com.google.firebase.messaging.*;
import com.lattechiffon.coinapi.domain.Coin;
import com.lattechiffon.coinapi.domain.Notification;
import com.lattechiffon.coinapi.dto.CoinDTO;
import com.lattechiffon.coinapi.dto.NotificationDTO;
import com.lattechiffon.coinapi.repository.NotificationRepository;
import com.lattechiffon.coinapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationDTO> getNotificationTokens(String username) {
        List<Notification> notifications = notificationRepository.findByUserAndIsTokenNotExpiredTrue(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The username is not found.")));

        List<NotificationDTO> notificationDTOs = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO(notification);

            notificationDTOs.add(notificationDTO);
        }

        return notificationDTOs;
    }

    @Override
    public void setNotificationToken(String username, String token) {
        Notification notification = new Notification();

        notification.setUser(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The username is not found.")));
        notification.setToken(token);
        notification.setIsTokenNotExpired(true);
        notificationRepository.save(notification);
    }

    @Override
    public int sendAll(String title, String content) throws FirebaseMessagingException {
        List<Notification> notifications = notificationRepository.findByIsTokenNotExpiredTrue();

        if (notifications.size() < 1) {
            return 0;
        }

        List<String> tokens = new ArrayList<>();

        for (Notification notification : notifications) {
            tokens.add(notification.getToken());
        }

        return this.sendAll(tokens, title, content);
    }

    @Override
    public int sendAll(List<String> tokens, String title, String content) throws FirebaseMessagingException {
        MulticastMessage message = MulticastMessage.builder()
                .putData("title", title)
                .putData("content", content)
                .addAllTokens(tokens)
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

        return response.getSuccessCount();
    }

    @Override
    public String send(String token, String title, String content) throws FirebaseMessagingException {
        Message message = Message.builder()
                .putData("title", title)
                .putData("content", content)
                .setToken(token)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}
