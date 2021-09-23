package com.williammacedo.bookstoremanager.user.controller;

import com.williammacedo.bookstoremanager.user.dto.JwtRequest;
import com.williammacedo.bookstoremanager.user.dto.JwtResponse;
import com.williammacedo.bookstoremanager.user.service.AuthenticationService;
import com.williammacedo.bookstoremanager.user.service.JwtTokenManager;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/auth")
@AllArgsConstructor
public class AuthenticationController implements AuthenticationControllerDocs {

    private AuthenticationService authenticationService;
    private AuthenticationManager authenticationManager;
    private JwtTokenManager jwtTokenManager;

    @PostMapping
    public JwtResponse autenticar(@RequestBody @Valid JwtRequest jwtRequest) {
        String username = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        UserDetails userDetails = authenticationService.loadUserByUsername(username);
        String token = jwtTokenManager.generateToken(userDetails);

        return new JwtResponse(token);
    }
}
