package com.groom.yummy.domain.user;

import com.groom.yummy.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserEntityRepositoryTest {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private UserEntity testUserEntity;

    @BeforeEach
    void setUp(){
        testUserEntity = UserEntity.builder()
                .email("email@gmail.com")
                .nickname("강형준")
                .role("ROLE_USER")
                .groupAttendanceCount(0L)
                .groupJoinCount(0L)
                .isDeleted(false)
                .build();

        userJpaRepository.save(testUserEntity);
    }

    @Test
    void findByIdTest() {
        // given
        Long userId = testUserEntity.getId();
        // when
        Optional<User> result = userEntityRepository.findById(userId);
        // then
        assertTrue(result.isPresent());
        assertEquals(testUserEntity.getId(), result.get().getId());
        assertEquals(testUserEntity.getEmail(), result.get().getEmail());
        assertEquals(testUserEntity.getNickname(), result.get().getNickname());
        assertEquals(testUserEntity.getRole(), result.get().getRole());
        assertEquals(testUserEntity.isDeleted(), result.get().isDeleted());
    }

    @Test
    void findByEmailTest() {
        // given
        String email = "email@gmail.com";

        // when
        Optional<User> result = userEntityRepository.findByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertEquals(testUserEntity.getId(),result.get().getId());
        assertEquals(testUserEntity.getEmail(), result.get().getEmail());
        assertEquals(testUserEntity.getNickname(), result.get().getNickname());
        assertEquals(testUserEntity.getRole(), result.get().getRole());
    }

    @Test
    void saveTest() {
        // given
        String email = "email@naver.com";
        String nickname = "홍길동";
        String role = "ROLE_USER";
        boolean isDeleted = false;
        User user = User.builder().email(email).nickname(nickname).role(role).groupJoinCount(0L).groupAttendanceCount(0L).isDeleted(isDeleted).build();

        // when
        User result = userEntityRepository.save(user);

        // then
        assertEquals(result.getEmail(), email);
        assertEquals(result.getNickname(), nickname);
        assertEquals(result.getRole(), role);
        assertEquals(result.isDeleted(), isDeleted);
    }
}