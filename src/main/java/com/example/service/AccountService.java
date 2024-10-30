package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account) {
        
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 4 characters long");
        }

        
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        
        return accountRepository.save(account);
    }

    public Optional<Account> login(String username, String password) {
        
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            
            if (account.getPassword().equals(password)) {
                return Optional.of(account);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

 
}

