package com.williammacedo.bookstoremanager.user.exception;

import javax.persistence.EntityExistsException;

public class UserAlreadyExistsException extends EntityExistsException {
    public UserAlreadyExistsException(String username, String email) {
        super(String.format("User with email %s or username %s already exists!", email, username));
    }
}
