package com.library.entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {
    @Id
    private ObjectId id;
    @DBRef
    private User user;
    @DBRef
    private Book book;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    // Getters and Setters
}