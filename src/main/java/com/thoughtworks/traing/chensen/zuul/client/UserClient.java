package com.thoughtworks.traing.chensen.zuul.client;


import com.thoughtworks.traing.chensen.zuul.dto.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

//url="http://localhost:8081",
@FeignClient(name = "userservice", serviceId = "userservice")
public interface UserClient {
    @PostMapping("/verification-internal")
    User verifyTokenInternal(String token);

    @PostMapping("/login")
    String login(User user);

}

