package com.lattechiffon.coinapi.dto;

import com.lattechiffon.coinapi.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationDTO {
    private Long id;
    private String username;
    private String token;
    private boolean isTokenNotExpired;

    public NotificationDTO(Notification notification) {
        this.setId(notification.getId());
        this.setUsername(notification.getUser().getUsername());
        this.setToken(notification.getToken());
        this.isTokenNotExpired = notification.getIsTokenNotExpired();
    }
}
