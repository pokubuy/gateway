package org.pokubuy.gateway.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.pokubuy.gateway")
public class App {

    static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
