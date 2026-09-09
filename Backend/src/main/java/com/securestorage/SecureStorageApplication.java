package com.securestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.securestorage")
@EnableJpaRepositories(basePackages = "com.securestorage.repository")
@EntityScan(basePackages = "com.securestorage.model")
public class SecureStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecureStorageApplication.class, args);
    }
}

