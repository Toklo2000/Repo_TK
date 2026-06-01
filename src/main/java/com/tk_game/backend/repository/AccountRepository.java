package com.tk_game.backend.repository;

import com.tk_game.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findByLogin(String login);
}
