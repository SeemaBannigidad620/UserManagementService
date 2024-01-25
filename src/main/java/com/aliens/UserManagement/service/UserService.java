package com.aliens.UserManagement.service;

import com.aliens.UserManagement.config.AppConfig;
import com.aliens.UserManagement.entity.ResponseBody;
import com.aliens.UserManagement.entity.User;
import com.aliens.UserManagement.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@Service
@Component
public class UserService {
    @Value("${identity.service.url}")
    private String identityServiceUrl;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> validateAndRegisterUser(User user, String authHeader) {
        try {
            String token = authHeader;
            ResponseBody response = validateToken(token);
            if (response.getStatus() == HttpStatus.OK) {
                User savedUser = userRepository.findByUserId(user.getUserId());
                if(!ObjectUtils.isEmpty(savedUser)){
                    throw new RuntimeException("User exists for the Id " +user.getUserId());
                }
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body("User registered successfully!");
            } else {
                throw new RuntimeException("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    private ResponseBody validateToken(String token) {
        String validateTokenUrl = identityServiceUrl + "/validate/token";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                validateTokenUrl,
                HttpMethod.GET,
                entity,
                String.class);


        String responseBodyString = responseEntity.getBody();
        ResponseBody responseBody = null;

        try {
            responseBody = objectMapper.readValue(responseBodyString, ResponseBody.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public List<User> fetchAllUsers() {
        List<User> users = userRepository.findAll();
        if(ObjectUtils.isEmpty(users)){
            throw new RuntimeException("Empty data");
        }
        return users;
    }

    public ResponseEntity<Object> updateUser(User user) {
        User existingUser = userRepository.findByUserId(user.getUserId());
        if(ObjectUtils.isEmpty(existingUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User does not exist. Data cannot to be updated");
        }
        updateUserData(user);
        return ResponseEntity.status(HttpStatus.OK).body("User Data Updated Successfully");
    }

    public void updateUserData(User user) {
       Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getUserId()));
        org.springframework.data.mongodb.core.query.Update update = new Update()
                .set("name", user.getName())
                .set("email", user.getEmail())
                .set("phone_number", user.getPhone_number())
                .set("address", user.getAddress());

        mongoTemplate.updateFirst(query, update, User.class);
    }




}
