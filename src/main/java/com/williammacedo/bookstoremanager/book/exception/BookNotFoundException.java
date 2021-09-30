package com.williammacedo.bookstoremanager.book.exception;

import javax.persistence.EntityNotFoundException;

public class BookNotFoundException extends EntityNotFoundException {
    public BookNotFoundException(Long id) {
        super(String.format("Book with id %s not exists!", id));
    }
}
