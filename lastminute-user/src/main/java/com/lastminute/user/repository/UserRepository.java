package com.lastminute.user.repository;

import com.lastminute.user.domain.AccountState;
import com.lastminute.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

}
