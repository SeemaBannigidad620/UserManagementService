package com.aliens.UserManagement.controller;

import com.aliens.UserManagement.entity.User;
import com.aliens.UserManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/management")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user, @RequestHeader("Authorization") String authHeader){
        return userService.validateAndRegisterUser(user, authHeader);
    }

    @GetMapping("/fetch/users")
    public List<User> fetchAllUsers(){
        return userService.fetchAllUsers();
    }

    @PutMapping("/update/user")
    public ResponseEntity<Object> updateDetails(@RequestBody User user){
        return userService.updateUser(user);
    }
}
