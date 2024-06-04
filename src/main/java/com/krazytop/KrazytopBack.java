package com.krazytop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class KrazytopBack {

    public static void main(String[] args) {
        SpringApplication.run(KrazytopBack.class, args);
    }
}
