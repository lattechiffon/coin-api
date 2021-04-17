package com.lattechiffon.coinapi.service;

import com.lattechiffon.coinapi.dto.UserDTO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    boolean login(UserDTO user) throws UsernameNotFoundException, BadCredentialsException;
    Long join(UserDTO user) throws IllegalArgumentException;
}
