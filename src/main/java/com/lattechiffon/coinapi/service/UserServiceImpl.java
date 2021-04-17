package com.lattechiffon.coinapi.service;

import com.lattechiffon.coinapi.domain.User;
import com.lattechiffon.coinapi.dto.UserDTO;
import com.lattechiffon.coinapi.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The username is not found."));
    }

    @Override
    public boolean login(UserDTO user) throws UsernameNotFoundException, BadCredentialsException {
        User member = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("The user is not found."));
        if (!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("The user is not found.");
        }

        user.setRoles(member.getRoles());

        return true;
    }

    @Override
    public Long join(UserDTO user) throws IllegalArgumentException {
        return userRepository.save(User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getId();
    }
}
