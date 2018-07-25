package com.springlogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springlogin.domain.Account;
import com.springlogin.domain.Role;
import com.springlogin.repository.AccountRepository;
import com.springlogin.repository.RoleRepository;

@Service
public class AccountService implements UserDetailsService {

  private final String USER_ROLE = "USER";

  private BCryptPasswordEncoder bcryptPasswordEncoder;

  private RoleRepository roleRepository;

  private AccountRepository accountRepository;

  @Autowired
  public void setRoleRepository(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Autowired
  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Autowired
  public void setBcryptPasswordEncoder(BCryptPasswordEncoder bcryptPasswordEncoder) {
    this.bcryptPasswordEncoder = bcryptPasswordEncoder;
  }

  //  @PostConstruct
  //  public void init() {
  //    Account acc = new Account("ssorojam", "Password123", "Password123", "ssorojam@gmail.com", "Daniel", "Majoross");
  //    accountRepository.save(acc);
  //  }

  public Account findByEmail(String email) {
    return accountRepository.findByEmail(email);
  }

  public Account findByConfirmationToken(String confirmationToken) {
    return accountRepository.findByConfirmationToken(confirmationToken);
  }

  public void saveAccount(Account account) {
    accountRepository.save(account);
  }

  public Account findByUsername(String username) {
    return accountRepository.findByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = findByUsername(username);
    if (account == null) {
      throw new UsernameNotFoundException(username);
    }
    return new UserDetailsImpl(account);
  }

  public void registerUser(Account account) {
    Role accountRole = roleRepository.findByRole(USER_ROLE);
    if (accountRole != null) {
      account.getRoles().add(accountRole);
    } else {
      account.addRoles(USER_ROLE);
    }
    account.setPassword(bcryptPasswordEncoder.encode(account.getPassword()));
    accountRepository.save(account);
  }
}
