package com.williammacedo.bookstoremanager.book.service;

import com.williammacedo.bookstoremanager.author.entity.Author;
import com.williammacedo.bookstoremanager.author.service.AuthorService;
import com.williammacedo.bookstoremanager.book.dto.BookRequestDTO;
import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.book.entity.Book;
import com.williammacedo.bookstoremanager.book.exception.BookAlreadyExistsException;
import com.williammacedo.bookstoremanager.book.exception.BookNotFoundException;
import com.williammacedo.bookstoremanager.book.mapper.BookMapper;
import com.williammacedo.bookstoremanager.book.repository.BookRepository;
import com.williammacedo.bookstoremanager.publisher.entity.Publisher;
import com.williammacedo.bookstoremanager.publisher.service.PublisherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BookService {

    private final BookMapper mapper = BookMapper.INSTANCE;

    private AuthorService authorService;
    private PublisherService publisherService;
    private BookRepository repository;

    public List<BookResponseDTO> findAll() {
        return mapper.booksToBooksDtos(repository.getBooksAuthorsAndPublishers());
    }

    public BookResponseDTO getById(Long id) {
        return mapper.toDTO(verifyAndGetBook(id));
    }

    public List<BookResponseDTO> getBooksByUser(String username) {
        return mapper.booksToBooksDtos(repository.getBooksByUser(username));
    }

    @Transactional
    public BookResponseDTO create(BookRequestDTO dto) {
        Author author = authorService.verifyAndGetAuthor(dto.getAuthorId());
        Publisher publisher = publisherService.verifyAndGet(dto.getPublisherId());
        verifyIfBookIsAlreadyRegistered(dto);

        Book bookToSave = mapper.toModel(dto);
        bookToSave.setAuthor(author);
        bookToSave.setPublisher(publisher);
        Book savedBook = repository.save(bookToSave);
        return mapper.toDTO(savedBook);
    }

    @Transactional
    public BookResponseDTO update(Long id, BookRequestDTO dto) {
        verifyIfBookIsAlreadyRegistered(id, dto);
        Book entity = verifyAndGetBook(id);

        Author author = authorService.verifyAndGetAuthor(dto.getAuthorId());
        Publisher publisher = publisherService.verifyAndGet(dto.getPublisherId());

        mapper.updateBook(entity, dto);
        entity.setAuthor(author);
        entity.setPublisher(publisher);

        Book savedBook = repository.save(entity);
        return mapper.toDTO(savedBook);
    }

    @Transactional
    public void delete(Long id) {
        Book book = verifyAndGetBook(id);
        repository.delete(book);
    }

    private Book verifyAndGetBook(Long id) {
        return repository.getBooksAuthorsAndPublishers(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    private void verifyIfBookIsAlreadyRegistered(BookRequestDTO dto) {
        repository.findByNameAndIsbn(dto.getName(), dto.getIsbn())
            .ifPresent(book -> {
                throw new BookAlreadyExistsException(book.getName(), book.getIsbn());
            });
    }

    private void verifyIfBookIsAlreadyRegistered(Long id, BookRequestDTO dto) {
        repository.findByNameAndIsbnAndIdNot(dto.getName(), dto.getIsbn(), id)
            .ifPresent(book -> {
                throw new BookAlreadyExistsException(book.getName(), book.getIsbn());
            });
    }
}
