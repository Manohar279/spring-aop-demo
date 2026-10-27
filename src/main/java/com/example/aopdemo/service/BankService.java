package com.example.aopdemo.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// Plain business logic: no logging code here. The aspect adds it from outside.
@Service
public class BankService {

    private final Map<String, Double> balances = new HashMap<>();

    public void deposit(String account, double amount) {
        balances.merge(account, amount, Double::sum);
        System.out.println("    [BankService] Deposited " + amount + " into " + account);
    }

    public double getBalance(String account) {
        return balances.getOrDefault(account, 0.0);
    }

    public void withdraw(String account, double amount) {
        double current = getBalance(account);
        if (amount > current) {
            throw new IllegalArgumentException("Insufficient funds in " + account);
        }
        balances.put(account, current - amount);
    }
}
