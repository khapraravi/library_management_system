package com.library.service;


import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.BorrowRequestDTO;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BorrowService {
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> issueBook(BorrowRequestDTO borrowRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try {
                if (borrowRequestDTO.getIsbn() == null) {
                    return ResponseEntity.status(400).body("ISBN cannot be null");
                }
                if (borrowRequestDTO.getUsername() == null) {
                    return ResponseEntity.status(400).body("Username cannot be null");
                }
                Book book = bookRepository.findBookByIsbn(borrowRequestDTO.getIsbn());
                if (book == null) {
                    return ResponseEntity.status(404).body("Book not found with isbn");
                }
                if (book.isAvailable() == false) {
                    return ResponseEntity.status(400).body("Book is already borrowed by someone");
                }

                User user = userRepository.findByUsername(borrowRequestDTO.getUsername());
                if (user == null) {
                    return ResponseEntity.status(404).body("User not found with username");
                }
                BorrowRecord borrowRecord = new BorrowRecord();
                borrowRecord.setBook(book);
                borrowRecord.setUser(user);
                borrowRecord.setBorrowDate(LocalDate.now());
                borrowRecord.setReturnDate(LocalDate.now().plusDays(14));
                borrowRepository.save(borrowRecord);


                book.setAvailable(false);
                bookRepository.save(book);
                return ResponseEntity.ok("Book issued successfully");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error issuing book: " + e.getMessage());
            }

        }else{
            return  new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity<?> returnBook(BorrowRequestDTO borrowRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated())
        {
            try {
                if (borrowRequestDTO.getIsbn() == null) {
                    return ResponseEntity.status(400).body("ISBN cannot be null");
                }
                if (borrowRequestDTO.getUsername() == null) {
                    return ResponseEntity.status(400).body("Username cannot be null");
                }
                Book book = bookRepository.findBookByIsbn(borrowRequestDTO.getIsbn());
                if (book == null) {
                    return ResponseEntity.status(404).body("Book not found with isbn");
                }
                User user = userRepository.findByUsername(borrowRequestDTO.getUsername());
                if (user == null) {
                    return ResponseEntity.status(404).body("User not found with username");
                }

                BorrowRecord borrowRecord = borrowRepository.findByBookAndUser(book, user);
                if (borrowRecord == null) {
                    return ResponseEntity.status(404).body("Borrow record not found");
                }
                book.setAvailable(true);
                bookRepository.save(book);
                borrowRepository.delete(borrowRecord);
                return ResponseEntity.ok("Book returned successfully");
            }catch (Exception e)
            {
                return ResponseEntity.status(500).body("Error returning book: " + e.getMessage());
            }
        }else{
            return  new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }


    }
}
