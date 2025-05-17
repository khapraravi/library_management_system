package com.library.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NotBlank(message = "title is mandatory")
    private String title;
    @NotBlank(message = "author is mandatory")
    private String author;
    @Indexed(unique = true)
    @NotBlank(message = "isbn is mandatory")
    @Pattern(
            regexp = "^IS\\d{2}$",
            message = "ISBN must start with 'IS' followed by 2 digits, total length 4"
    )
    private String isbn;
    private boolean available = true;
}




