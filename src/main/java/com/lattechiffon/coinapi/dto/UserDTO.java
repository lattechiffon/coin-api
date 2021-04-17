package com.lattechiffon.coinapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private List<String> roles;
    private List<Long> coins;
    private List<Long> notifications;
}
