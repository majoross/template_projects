package com.springlogin.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.springlogin.domain.Account;
import com.springlogin.service.AccountService;
import com.springlogin.service.EmailService;
import com.springlogin.validator.AccountValidator;

@Controller
public class RegistryController {

  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private AccountService accountService;
  private EmailService emailService;

  private AccountValidator accountValidator;

  @Autowired
  public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Autowired
  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }

  @Autowired
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  @Autowired
  public void setUserValidator(AccountValidator accountValidator) {
    this.accountValidator = accountValidator;
  }

  @RequestMapping(value = "/registration", method = RequestMethod.GET)
  public String registration(Model model) {
    model.addAttribute("account", new Account());
    return "registration";
  }

  @RequestMapping(value = "/reg", method = RequestMethod.POST)
  public ModelAndView registry(ModelAndView modelAndView, @Valid Account account, BindingResult bindingResult, HttpServletRequest request) {
    //    if (account != null) {
    Account accountExists = accountService.findByEmail(account.getEmail());
    if (accountExists != null) {
      modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the email provided.");
      modelAndView.setViewName("registration");
      bindingResult.reject("email");
    }

    accountValidator.validate(account, bindingResult);

    if (bindingResult.hasErrors()) {
      modelAndView.setViewName("registration");
    } else { // new user so we create user and send confirmation e-mail

      // Disable user until they click on confirmation link in email
      account.setEnabled(false);

      // Generate random 36-character string token for confirmation link
      account.setConfirmationToken(UUID.randomUUID().toString());
      accountService.registerUser(account);
      String appUrl = request.getScheme() + "://" + request.getServerName();

      SimpleMailMessage registrationEmail = new SimpleMailMessage();
      registrationEmail.setTo(account.getEmail());
      registrationEmail.setSubject("Registration Confirmation");
      registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" + appUrl + "/confirm?token=" + account.getConfirmationToken());
      registrationEmail.setFrom("majoross.d@gmail.com");

      emailService.sendEmail(registrationEmail);

      modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " + account.getEmail());
      modelAndView.setViewName("registration");
      return modelAndView;

    }

    //    }

    return modelAndView;
  }

  //Process confirmation link
  @RequestMapping(value = "/confirm", method = RequestMethod.GET)
  public ModelAndView showConfirmationPage(ModelAndView modelAndView, @RequestParam("token") String token) {

    Account account = accountService.findByConfirmationToken(token);

    if (account == null) { // No token found in DB
      modelAndView.addObject("invalidToken", "Oops!  This is an invalid confirmation link.");
    } else { // Token found
      modelAndView.addObject("confirmationToken", account.getConfirmationToken());
    }

    modelAndView.setViewName("confirm");
    return modelAndView;
  }

  // Process confirmation link
  @RequestMapping(value = "/confirm", method = RequestMethod.POST)
  public ModelAndView processConfirmationForm(ModelAndView modelAndView, BindingResult bindingResult, @RequestParam Map requestParams, RedirectAttributes redir) {

    modelAndView.setViewName("confirm");

    Zxcvbn passwordCheck = new Zxcvbn();

    Strength strength = passwordCheck.measure((String) requestParams.get("password"));

    if (strength.getScore() < 3) {
      bindingResult.reject("password");

      redir.addFlashAttribute("errorMessage", "Your password is too weak.  Choose a stronger one.");

      modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
      System.out.println(requestParams.get("token"));
      return modelAndView;
    }

    // Find the user associated with the reset token
    Account account = accountService.findByConfirmationToken((String) requestParams.get("token"));

    // Set new password
    account.setPassword(bCryptPasswordEncoder.encode((CharSequence) requestParams.get("password")));

    // Set user to enabled
    account.setEnabled(true);

    // Save user
    accountService.saveAccount(account);
    modelAndView.addObject("successMessage", "Your password has been set!");
    return modelAndView;
  }

}
