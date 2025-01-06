package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    //Finds account by username
    Optional<Account> findByUsername(String username);
    //Finds account by username and password
    Optional<Account> findByUsernameAndPassword(String username, String Password);
}
