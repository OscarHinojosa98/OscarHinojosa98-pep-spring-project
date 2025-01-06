package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    //Create Account
    public Account addNewAccount(Account account){
        //Duplicate Username
        if (!accountRepository.findByUsername(account.getUsername()).isEmpty()){
            return new Account("","");
        }
        //Meets Username and Password requiremnts
        else if(!account.getUsername().isBlank() && account.getPassword().length() > 3){
            Account acc = new Account(account.getUsername(), account.getPassword());
            accountRepository.save(acc);
            return acc;
        }
        //Fails
        else
        {
        return null;
        }
    }
    //Finds account with username and password provided
    public Optional<Account> login(Account account){
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
    }
}
