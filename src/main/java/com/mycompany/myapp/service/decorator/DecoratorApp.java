package com.mycompany.myapp.service.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DecoratorApp implements CommandLineRunner {

    @Autowired
    @Qualifier("delegate")
    private IExampleService delegate;

    @Autowired
    private IExampleService primaryService;

    @Autowired
    @Qualifier("2ndDecorator")
    private IExampleService secondService;

    @Override
    public void run(String... args) throws Exception {

        System.out.println(delegate.startExample());
        System.out.println(primaryService.startExample());
        System.out.println(secondService.startExample());
    }

    public static void main(String[] args) {
        SpringApplication.run(DecoratorApp.class, args);
    }

}
