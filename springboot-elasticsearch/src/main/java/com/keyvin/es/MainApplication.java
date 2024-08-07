package com.keyvin.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author weiwh
 * @date 2020/2/24 23:52
 */
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);
        System.out.println("Swagger address: http://localhost:8080/es/swagger-ui.html");
    }
}
