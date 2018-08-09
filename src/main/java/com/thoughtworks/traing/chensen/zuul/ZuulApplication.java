package com.thoughtworks.traing.chensen.zuul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableZuulProxy
@SpringBootApplication
public class ZuulApplication {


	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}
}
