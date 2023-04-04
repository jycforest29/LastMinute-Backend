package com.lastminute.user.service;

import com.lastminute.user.domain.User;
import com.lastminute.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserWriteFacade {

    private final UserRepository userRepository;
    // TODO kafka producer

    @Transactional
    public User createUser(User user) {
        user = userRepository.save(user);
        // TODO msg produce

        return user;
    }

    @Transactional
    public User withdrawUser(User user) {
        user.withdraw();
        user = userRepository.save(user);
        // TODO msg produce

        return user;
    }

    @Transactional
    public User updateProfile(User user) {
        user = userRepository.save(user);

        // TODO msg produce
        return user;
    }
}
