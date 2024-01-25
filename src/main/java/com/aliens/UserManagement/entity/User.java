package com.aliens.UserManagement.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "user_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {


    int userId;

    String name;

    String email;

    long phone_number;

    String address;

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User(int userId, String name, String email, long phone_number, String address) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
    }
}
