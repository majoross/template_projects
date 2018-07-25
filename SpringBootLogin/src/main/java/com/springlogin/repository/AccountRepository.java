package com.springlogin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springlogin.domain.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

  Account findByEmail(String email);

  Account findByConfirmationToken(String confirmationToken);

  Account findByUsername(String username);
}
