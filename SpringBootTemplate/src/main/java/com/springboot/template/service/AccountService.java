package com.springboot.template.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.template.domain.Account;
import com.springboot.template.repository.AccountRepository;

@Service
public class AccountService {

  private AccountRepository accountRepository;

  @Autowired
  public void setAccountRepository(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @PostConstruct
  public void init() {
    Account acc = new Account("ssorojam", "Password123", "ssorojam@gmail.com", "Daniel", "Majoross");
    accountRepository.save(acc);
  }

}
