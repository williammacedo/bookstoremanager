package com.williammacedo.bookstoremanager.user.service;

import com.williammacedo.bookstoremanager.user.dto.AuthenticatedUser;
import com.williammacedo.bookstoremanager.user.dto.UserDetailsDTO;
import com.williammacedo.bookstoremanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsDTO user = repository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException(String.format("User not found with username %s", username))
        );
        return new AuthenticatedUser(user.getUsername(), user.getPassword(), user.getRole().name());
    }
}
