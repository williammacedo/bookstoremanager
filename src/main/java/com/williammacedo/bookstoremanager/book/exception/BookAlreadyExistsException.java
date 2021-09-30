package com.williammacedo.bookstoremanager.book.exception;

import javax.persistence.EntityExistsException;

public class BookAlreadyExistsException extends EntityExistsException {
    public BookAlreadyExistsException(String name, String isbn) {
        super(String.format("Book with name %s and ISBN %s already registered!", name, isbn));
    }
}
