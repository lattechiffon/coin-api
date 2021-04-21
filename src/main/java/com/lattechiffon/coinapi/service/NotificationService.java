package com.lattechiffon.coinapi.service;

import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface NotificationService {
    List<String> getNotificationTokens(String username);
    void setNotificationToken(String username, String token);
    int sendAll(String title, String content)  throws FirebaseMessagingException;
    int sendAll(List<String> tokens, String title, String content) throws FirebaseMessagingException;
    String send(String token, String title, String content) throws FirebaseMessagingException;
}
