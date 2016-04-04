package com.mycompany.myapp.service.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrimaryServiceApp implements CommandLineRunner {

    @Autowired
    private IExampleService primaryService;

    @Override
    public void run(String... args) throws Exception {

        System.out.println(primaryService.startExample());

    }

    public static void main(String[] args) {
        SpringApplication.run(PrimaryServiceApp.class, args);
    }

}
