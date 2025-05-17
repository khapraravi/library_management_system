package com.library.controller;


import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.User;
import com.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        return libraryService.addBook(book);
    }

    @PostMapping("/users/add")
    public User addUser(@RequestBody User user) {
        return libraryService.addUser(user);
    }

    @PostMapping("/issue")
    public BorrowRecord issueBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return libraryService.issueBook(userId, bookId);
    }

    @PostMapping("/return")
    public BorrowRecord returnBook(@RequestParam Long recordId) {
        return libraryService.returnBook(recordId);
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return libraryService.getAllBooks();
    }
}
