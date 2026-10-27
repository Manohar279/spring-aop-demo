package com.example.aopdemo;

import com.example.aopdemo.service.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AopDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopDemoApplication.class, args);
    }

    // Runs once at startup and calls the service, so the aspect output is printed.
    @Bean
    CommandLineRunner demo(BankService bankService) {
        return args -> {
            System.out.println("\n===== 1. Normal method call =====");
            bankService.deposit("ACC-101", 500.0);

            System.out.println("\n===== 2. Method with a return value =====");
            double balance = bankService.getBalance("ACC-101");
            System.out.println("Main got balance: " + balance);

            System.out.println("\n===== 3. Method that throws an exception =====");
            try {
                bankService.withdraw("ACC-101", 10_000.0);
            } catch (IllegalArgumentException e) {
                System.out.println("Main caught: " + e.getMessage());
            }
        };
    }
}
