package com.example.bankappapplication.repository;

import com.example.bankappapplication.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findAllByCustomerId(Long customerId);
}
