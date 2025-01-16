package com.groom.yummy.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;

    public Optional<User> findAuthUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
