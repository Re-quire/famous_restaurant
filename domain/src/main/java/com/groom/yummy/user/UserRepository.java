package com.groom.yummy.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Long save(User user);
}
