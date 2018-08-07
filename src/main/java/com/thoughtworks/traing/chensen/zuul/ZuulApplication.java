package com.thoughtworks.traing.chensen.zuul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class ZuulApplication {


	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}
}
