package com.lattechiffon.coinapi.controller;

import com.lattechiffon.coinapi.config.security.JwtTokenProvider;
import com.lattechiffon.coinapi.dto.UserDTO;
import com.lattechiffon.coinapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public String login(@RequestBody UserDTO user) {
        if (user.getUsername().equals("") || user.getPassword().equals("")) return "empty";

        try {
            if (userService.login(user)) {
                return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            }
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return "fail";
        }

        return "fail";
    }

    @PostMapping("/join")
    public String join(@RequestBody UserDTO user) {
        try {
            return userService.join(user) > 0 ? "success" : "fail";
        } catch (Exception e) {
            return "fail";
        }
    }
}
