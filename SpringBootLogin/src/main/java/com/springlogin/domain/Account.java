package com.springlogin.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Table(name = "account")
@Entity
public class Account {

  @GeneratedValue
  @Id
  private Long id;

  @Column
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String passwordConfirm;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "accounts_roles", joinColumns = {@JoinColumn(name = "account_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private Set<Role> roles = new HashSet<Role>();


  @Column(name = "email", nullable = false, unique = true)
  @Email(message = "Please provide a valid e-mail")
  @NotEmpty(message = "Please provide an e-mail")
  private String email;

  @Column
  @NotEmpty(message = "Please provide your first name")
  private String firstName;

  @Column
  @NotEmpty(message = "Please provide your last name")
  private String lastName;

  @Column(name = "enabled")
  private boolean enabled;

  @Column(name = "confirmation_token")
  private String confirmationToken;


  public Account() {

  }

  public Account(String username, String password, String passwordConfirm, @Email String email, String firstName, String lastName) {
    this.username = username;
    this.password = password;
    this.passwordConfirm = passwordConfirm;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Transient
  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getConfirmationToken() {
    return confirmationToken;
  }

  public void setConfirmationToken(String confirmationToken) {
    this.confirmationToken = confirmationToken;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public void addRoles(String roleName) {
    if (this.roles == null || this.roles.isEmpty()) {
      this.roles = new HashSet<>();
    }
    this.roles.add(new Role(roleName));
  }

}
