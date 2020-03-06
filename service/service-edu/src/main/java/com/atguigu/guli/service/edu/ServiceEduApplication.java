package com.atguigu.guli.service.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({"com.atguigu.guli"})
@EnableFeignClients
@EnableCircuitBreaker
public class ServiceEduApplication {
    public static void main(String[] args) {

        SpringApplication.run(ServiceEduApplication.class, args);
    }
}
