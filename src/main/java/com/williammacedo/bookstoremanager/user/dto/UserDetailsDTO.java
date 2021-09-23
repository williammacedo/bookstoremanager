package com.williammacedo.bookstoremanager.user.dto;

import com.williammacedo.bookstoremanager.user.enums.Role;
import lombok.Value;

@Value
public class UserDetailsDTO {
    String username;
    String password;
    Role role;
}
