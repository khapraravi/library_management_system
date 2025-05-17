package com.library.service;

import com.library.entity.User;
import com.library.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final String regex = "^(ADMIN|USER)$";
    Pattern pattern = Pattern.compile(regex);

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public ResponseEntity<?> saveUser(User user) throws MethodArgumentNotValidException, HttpMessageNotReadableException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try {
                User userInDb = userRepository.findByUsername(user.getUsername());
                if (userInDb != null) {
                    return ResponseEntity.status(201).body("USER ALREADY EXISTS");
                }
                List<String> filteredRoles = user.getRoles().stream()
                        .filter(role -> pattern.matcher(role).matches())
                        .collect(Collectors.toList());
                if(filteredRoles.isEmpty() || filteredRoles == null)
                    throw new HttpMessageNotReadableException("Invalid role");
                user.setRoles(filteredRoles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.CREATED).body("New User Created");
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user: " + e.getMessage());
            }
        }else{
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        }


    }
    public ResponseEntity<?> getUserByUsername(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try{
                if (username == null || username.isEmpty()) {
                    throw new IllegalArgumentException("Username cannot be null or empty");
                }else{
                    User user = userRepository.findByUsername(username);
                    if (user == null) {
                        throw new IllegalArgumentException("User not found");
                    }else{
                        return new ResponseEntity<>(user, HttpStatus.OK);
                    }
                }
            }catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Error retrieving user: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving user: " + e.getMessage());
            }
        } else return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
    }
    public ResponseEntity<?> getUserById(ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try {
                if (id == null || id.equals("")) {
                    throw new IllegalArgumentException("ID cannot be null or empty");
                } else {
                    User user = userRepository.findById(id).orElse(null);
                    if (user == null) {
                        throw new IllegalArgumentException("User not found");
                    } else {
                        return new ResponseEntity<>(user, HttpStatus.OK);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Error retrieving user: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException("Error retrieving user: " + e.getMessage());
            }
        }
        else {
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        }

    }
    public ResponseEntity<?> updateUser(@Valid User user) throws MethodArgumentNotValidException, HttpMessageNotReadableException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try {
                User userInDb = userRepository.findById(user.getId()).orElse(null);
                if (userInDb == null) {
                    return ResponseEntity.status(404).body("User not found");
                }
                List<String> filteredRoles = user.getRoles().stream()
                        .filter(role -> pattern.matcher(role).matches())
                        .collect(Collectors.toList());
                if(filteredRoles.isEmpty() || filteredRoles == null)
                    throw new HttpMessageNotReadableException("Invalid role");
                user.setRoles(filteredRoles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
            }
        }else{
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        }


    }
    public ResponseEntity<?> deleteUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            try {
                User user = userRepository.findByUsername(username);
                if (user == null) {
                    return ResponseEntity.status(404).body("User not found");
                }
                userRepository.delete(user);
                return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user: " + e.getMessage());
            }
        }else{
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        }

    }
}