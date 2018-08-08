package com.thoughtworks.traing.chensen.zuul.controller;

import com.thoughtworks.traing.chensen.zuul.client.UserClient;
import com.thoughtworks.traing.chensen.zuul.dto.User;
import com.thoughtworks.traing.chensen.zuul.securtity.ToDoAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {

    @Autowired
    UserClient userClient;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        String id = userClient.login(user);
        try {
            String token = ToDoAuthFilter.generateToken(Integer.parseInt(id), user.getUserName());
            return token;

        } catch (Exception e) {
            return id;
        }
    }
}
