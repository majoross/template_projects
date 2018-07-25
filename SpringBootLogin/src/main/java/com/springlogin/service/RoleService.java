package com.springlogin.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springlogin.domain.Role;
import com.springlogin.repository.RoleRepository;

@Service
public class RoleService {

  private RoleRepository roleRepository;

  @Autowired
  public void setRoleRepository(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @PostConstruct
  public void init() {
    Role userRole = new Role("USER");
    roleRepository.save(userRole);
  }
}
