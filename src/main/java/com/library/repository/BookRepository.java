package com.library.repository;

import com.library.entity.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<Book, ObjectId> {

    Book findBookByIsbn(String isbn);
}

