package com.library.repository;

import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends MongoRepository<BorrowRecord, ObjectId> {

    BorrowRecord findByBookAndUser(Book book, User user);
}