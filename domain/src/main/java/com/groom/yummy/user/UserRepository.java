package com.groom.yummy.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);
    User save(User user);
}
