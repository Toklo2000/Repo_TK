package com.tk_game.backend.service;

import com.tk_game.backend.model.Account;
import com.tk_game.backend.repository.AccountRepository;
import com.tk_game.backend.dto.RegisterRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  
  public AuthService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }
  
  public Account register(RegisterRequestDTO request) {
/*    if (accountRepository.existsByLogin(request.getLogin())) {
      throw new RuntimeException("Login taken");
    }

    if (accountRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email taken");
    }
*/
    Account acc = new Account();
    acc.setLogin(request.getLogin());
    acc.setEmail(request.getEmail());
    acc.setPasswordHash(passwordEncoder.encode(request.getPassword()));

    return accountRepository.save(acc);
  }
}

