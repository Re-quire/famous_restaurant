package com.groom.yummy.handler;

import com.groom.yummy.domain.user.UserEntity;
import com.groom.yummy.domain.user.UserJpaRepository;
import com.groom.yummy.user.event.UserDeleteEvent;
import com.groom.yummy.user.event.UserNicknameChangedEvent;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserEventHandlerTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
    @DirtiesContext //테스트 실행 후에 Spring 애플리케이션 컨텍스트를 재생성, 트랜잭션 더러워져서 발생한 문제 일단 해결
    void handlerNicknameChaneTest(){
        // given
        Long userId = testUserEntity.getId();
        String newNickname = "새로운강형준";

        // when
        eventPublisher.publishEvent(new UserNicknameChangedEvent(userId, newNickname));

        // 해당 로직은 커밋 이전에 반영되모르 명시적으로 커밋을 설정한다.
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // then
        Optional<UserEntity> updatedUser = userJpaRepository.findById(userId);
        assertTrue(updatedUser.isPresent());
        assertEquals(newNickname, updatedUser.get().getNickname());
    }

    @Test
    @DirtiesContext //테스트 실행 후에 Spring 애플리케이션 컨텍스트를 재생성, 트랜잭션 더러워져서 발생한 문제 일단 해결
    void handleUserDeleteTest(){

        // given
        Long userId = testUserEntity.getId();
        boolean isDeleted = true;

        // when
        eventPublisher.publishEvent(new UserDeleteEvent(userId, isDeleted));

        // 해당 로직은 커밋 이전에 반영되모르 명시적으로 커밋을 설정한다.
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // then
        Optional<UserEntity> deletedUser = userJpaRepository.findById(userId);
        assertTrue(deletedUser.isPresent());
        assertTrue(deletedUser.get().isDeleted());
    }
}