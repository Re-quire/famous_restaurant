package com.groom.yummy.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {
    @InjectMocks
    private UserAuthService userAuthService;
    @Mock
    private UserRepository userRepository;

    @Test
    void findAuthUserByEmailTest() {
        // given
        String nickname = "강형준";
        String email = "email@gmail.com";
        String role = "ROLE_USER";
        User mockUser = User.builder()
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        Optional<User> result = userAuthService.findAuthUserByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals(nickname, result.get().getNickname());
    }

    @Test
    void findOrCreateUser_Create_Test() {
        // given
        Long userId = 1L;
        String nickname = "강형준";
        String email = "email@gmail.com";
        String role = "ROLE_USER";
        Optional<User> optionalUser = Optional.empty();

        User createUser = User.builder().id(userId).email(email).nickname(nickname).role(role).build();
        when(userRepository.save(any(User.class))).thenReturn(createUser);

        // when
        User result = userAuthService.findOrCreateUser(optionalUser, nickname, email);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(email, result.getEmail());
        assertEquals(nickname, result.getNickname());
        assertEquals(role, result.getRole());
    }

    @Test
    void findOrCreateUser_Find_Test() {
        // given
        String nickname = "강형준";
        String email = "email@gmail.com";
        String role = "ROLE_USER";

        User existingUser = User.builder()
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();

        Optional<User> optionalUser = Optional.of(existingUser);

        // when
        User result = userAuthService.findOrCreateUser(optionalUser, nickname, email);

        // then
        assertNotNull(result);
        assertEquals(existingUser, result);
    }
}