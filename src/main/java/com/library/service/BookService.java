package com.library.service;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.Valid;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public ResponseEntity<?> saveBook(Book book) throws MethodArgumentNotValidException, HttpMessageNotReadableException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try{
                Book bookInDb = bookRepository.findBookByIsbn(book.getIsbn());
                if (bookInDb != null) {
                    return ResponseEntity.status(201).body("BOOK ALREADY EXISTS");
                }
                bookRepository.save(book);
                return ResponseEntity.status(201).body("New Book Created");
            }catch (Exception e){
                return ResponseEntity.status(500).body("Error creating book: " + e.getMessage());
            }
        }else{
            return  new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }


    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public ResponseEntity<?> getBookByIsbn(String isbn) throws MethodArgumentNotValidException,HttpMessageNotReadableException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try{
                if (isbn == null || isbn.isEmpty()) {
                    throw new IllegalArgumentException("ISBN cannot be null or empty");
                }else{
                    Book book = bookRepository.findBookByIsbn(isbn);
                    if (book == null) {
                        throw new IllegalArgumentException("Book not found");
                    }else{
                        return ResponseEntity.ok(book);
                    }
                }
            }catch (Exception e){
                return null;
            }
        }else{
            return  new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity<?> deleteBook(@Valid String isbn) throws MethodArgumentNotValidException,HttpMessageNotReadableException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try {
                Book book = bookRepository.findBookByIsbn(isbn);
                if (book == null) {
                    return ResponseEntity.status(404).body("Book not found");
                }
                bookRepository.delete(book);
                return ResponseEntity.status(200).body("Book deleted successfully");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error deleting book: " + e.getMessage());
            }
        }else{
            return  new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }



    }

    public ResponseEntity<?> updateBook(Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Book bookIndb = bookRepository.findBookByIsbn(book.getIsbn());
            if(bookIndb == null) {
                return ResponseEntity.status(404).body("Book not found");
            }
            try {
                bookIndb.setTitle(book.getTitle());
                bookIndb.setAuthor(book.getAuthor());
                bookIndb.setAvailable(book.isAvailable());
                bookRepository.save(bookIndb);
                return ResponseEntity.status(200).body("Book updated successfully");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error updating book: " + e.getMessage());
            }
        }else{
            return  new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }


    }
}