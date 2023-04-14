package com.poseidon.api;

import com.poseidon.api.config.UserInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = com.poseidon.api.config.AppConfig.class)
public class SpringbootApplication implements CommandLineRunner {

    private UserInjector userInjector;

    @Autowired
    public SpringbootApplication(UserInjector userInjector) {
        this.userInjector = userInjector;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userInjector.init();
    }
}
