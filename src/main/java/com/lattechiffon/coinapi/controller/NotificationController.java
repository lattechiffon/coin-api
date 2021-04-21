package com.lattechiffon.coinapi.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.lattechiffon.coinapi.config.security.JwtTokenProvider;
import com.lattechiffon.coinapi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "")
    public List<String> getNotificationTokens(@RequestHeader("Authorization") String token) {
        return notificationService.getNotificationTokens(jwtTokenProvider.getUserIdx(token));
    }

    @PostMapping(value = "")
    public String setNotificationToken(@RequestHeader("Authorization") String token, @RequestBody String notificationToken) {
        try {
            notificationService.setNotificationToken(jwtTokenProvider.getUserIdx(token), notificationToken);
        } catch (Exception e) {
            return "fail";
        }

        return "success";
    }

    @PostMapping(value = "/send")
    public String send(@RequestBody Map<String, String> data) throws FirebaseMessagingException {
        return notificationService.send(data.get("token"), "공지사항", data.get("content"));
    }

    @PostMapping(value = "/send/all")
    public String sendAll(@RequestBody String content) throws FirebaseMessagingException {
        return String.valueOf(notificationService.sendAll("공지사항", content));
    }
}
