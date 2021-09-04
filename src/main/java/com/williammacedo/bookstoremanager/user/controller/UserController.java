package com.williammacedo.bookstoremanager.user.controller;

import com.williammacedo.bookstoremanager.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/users")
@AllArgsConstructor
public class UserController {

    private UserService service;
}
