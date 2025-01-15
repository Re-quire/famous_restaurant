package com.groom.yummy.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserEntityRepository {
    private final UserJpaRepository userJpaRepository;
}
