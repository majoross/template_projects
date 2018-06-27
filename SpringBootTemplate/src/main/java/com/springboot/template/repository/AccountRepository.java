package com.springboot.template.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.template.domain.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}
