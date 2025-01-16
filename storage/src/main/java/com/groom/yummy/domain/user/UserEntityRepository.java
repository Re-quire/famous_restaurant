package com.groom.yummy.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserEntityRepository implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public Long save(User user) {
        return userJpaRepository.save(UserEntity.toEntity(user)).getId();
    }
}
