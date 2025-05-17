package com.library.entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.List;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private ObjectId id;
    private String name;
    @NotBlank(message = "Username is mandatory")
    @Indexed(unique = true)
    @Size(min=3, message = "Username must be at least 3 characters")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, message = "Password must be at least 6 characters")
    private String password;
    private List<String> roles;
}
