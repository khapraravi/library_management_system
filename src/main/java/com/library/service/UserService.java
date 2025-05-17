package com.library.service;

import com.library.entity.User;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public ResponseEntity<?> saveUser(User user) {
        User userInDb = userRepository.findByUserName(user.getUsername());
        if(userInDb != null){
            return ResponseEntity.status(201).body("USER ALREADY EXISTS");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("New User Created");
    }
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }
    public ResponseEntity<?> getUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.FOUND);
    }

    public ResponseEntity<?> updateUser(User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User userInDb = userRepository.findById(user.getId()).orElse(null);
            if(userInDb == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }else{
                if(user.getPassword() != null){
                    userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                if(user.getUsername() != null){
                    userInDb.setUsername(user.getUsername());
                }
                if(user.getRoles() != null){
                    userInDb.setRoles(user.getRoles());
                }


                userRepository.save(userInDb);
                return ResponseEntity.status(201).body("User Updated");
            }
        }
        return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> deleteUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User user = userRepository.findById(id).orElse(null);
            if(user == null){
                return ResponseEntity.status(201).body("User Not found");
            }else{
                userRepository.deleteById(id);
                return ResponseEntity.status(201).body("User Deleted");
            }
        }
        return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> getUserByUserName(String username) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            User user = userRepository.findByUserName(username);
            if(user == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(user,HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
    }
}


