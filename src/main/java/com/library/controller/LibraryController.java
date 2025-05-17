package com.library.controller;


import com.library.entity.Book;
import com.library.entity.BorrowRequestDTO;
import com.library.entity.User;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/private")
public class LibraryController {
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowService borrowService;
    @PostMapping("/adduser")
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user) throws MethodArgumentNotValidException, HttpMessageNotReadableException{
        try{
            return userService.saveUser(user);
        }catch (MethodArgumentNotValidException | HttpMessageNotReadableException e){
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }

    }
    @GetMapping("/getuser")
    public ResponseEntity<?> getUser(@RequestParam String username) {
        try {
            return userService.getUserByUsername(username);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving user: " + e.getMessage());
        }
    }
    @GetMapping("/getuser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable ObjectId id) {
        try {
            return  userService.getUserById(id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving user: " + e.getMessage());
        }
    }
    @PutMapping("/updateuser")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) throws MethodArgumentNotValidException,HttpMessageNotReadableException {
        try {
            return userService.updateUser(user);
        } catch (MethodArgumentNotValidException | HttpMessageNotReadableException e) {
            return ResponseEntity.status(500).body("Error updating user: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(@RequestParam String username) {
        try {
            return userService.deleteUser(username);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user: " + e.getMessage());
        }
    }
    @PostMapping("/addbook")
    public ResponseEntity<?> saveBook(@Valid @RequestBody Book book) throws MethodArgumentNotValidException,HttpMessageNotReadableException {
        try {
            return bookService.saveBook(book);
        } catch (MethodArgumentNotValidException | HttpMessageNotReadableException e) {
            return ResponseEntity.status(500).body("Error creating book: " + e.getMessage());
        }
    }
    @DeleteMapping("/deletebook")
    public ResponseEntity<?> deleteBook(@Valid @RequestParam String isbn) throws MethodArgumentNotValidException,HttpMessageNotReadableException{
        try {
            return bookService.deleteBook(isbn);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting book: " + e.getMessage());
        }
    }
    @GetMapping("/getAllBooks")
    public ResponseEntity<?> getAllBooks() {
        try {
            return ResponseEntity.ok(bookService.getAllBooks());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving books: " + e.getMessage());
        }
    }
    @GetMapping("/getBook")
    public ResponseEntity<?> getBook(@Valid @RequestParam String isbn) throws MethodArgumentNotValidException,HttpMessageNotReadableException{
        try {
            return bookService.getBookByIsbn(isbn);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving book: " + e.getMessage());
        }
    }

    @PutMapping("/updatebook")
    public ResponseEntity<?> updateBookByIsbn(@RequestBody Book book) {
        try {
            return bookService.updateBook(book);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting book: " + e.getMessage());
        }
    }
    @PostMapping("/issuebook")
    public ResponseEntity<?> issueBook(@RequestBody BorrowRequestDTO borrowRequestDTO) {
        try {
            return borrowService.issueBook(borrowRequestDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error issuing book: " + e.getMessage());
        }
    }
    @PostMapping("/returnbook")
    public ResponseEntity<?> returnBook(@RequestBody BorrowRequestDTO borrowRequestDTO) {
        try {
            return borrowService.returnBook(borrowRequestDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error returning book: " + e.getMessage());
        }
    }

}