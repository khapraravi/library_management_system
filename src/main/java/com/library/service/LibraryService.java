package com.library.service;


import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public BorrowRecord issueBook(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        if (!book.isAvailable()) throw new RuntimeException("Book not available");

        User user = userRepository.findById(userId).orElseThrow();
        book.setAvailable(false);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setBorrowDate(LocalDate.now());

        return borrowRecordRepository.save(record);
    }

    public BorrowRecord returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId).orElseThrow();
        record.setReturnDate(LocalDate.now());

        Book book = record.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
