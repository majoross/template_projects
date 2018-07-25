package com.springlogin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springlogin.domain.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

  Role findByRole(String name);

}
