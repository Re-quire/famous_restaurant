package com.groom.yummy.facade;

import com.groom.yummy.publisher.EventPublisher;
import com.groom.yummy.user.dto.request.UpdateNicknameReqDto;
import com.groom.yummy.user.dto.response.UserInfoResDto;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserAuthService;
import com.groom.yummy.user.UserService;
import com.groom.yummy.user.facade.UserFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {
    @InjectMocks
    private UserFacade userFacade;
    @Mock
    private UserAuthService userAuthService;
    @Mock
    private UserService userService;

    @Test
    @DisplayName("완벽한 테스트")
    void getUserInfoTest() {
        // given
        Long userId = 1L;
        String nickname = "강형준";
        String email = "email@gmail.com";

        User mockUser = User.builder().id(userId).nickname(nickname).email(email).build();
        when(userService.getUserInfo(userId)).thenReturn(mockUser);

        // when
        UserInfoResDto result = userFacade.getUserInfo(userId);

        // then
        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.email());
        assertEquals(mockUser.getNickname(), result.nickname());
    }

    @Test
    @DisplayName("")
    void findAuthUserByEmailTest() {
        // given
        Long userId = 1L;
        String nickname = "강형준";
        String email = "email@gmail.com";

        User mockUser = User.builder().id(userId).nickname(nickname).email(email).build();
        when(userAuthService.findAuthUserByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        Optional<User> result = userFacade.findAuthUserByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    @DisplayName("")
    void findOrCreateUserTest() {
        // given
        Long userId = 1L;
        String nickname = "강형준";
        String email = "email@gmail.com";
        Optional<User> optionalUser = Optional.empty();
        User mockUser = User.builder().id(userId).nickname(nickname).email(email).build();
        when(userAuthService.findOrCreateUser(optionalUser, nickname, email)).thenReturn(mockUser);

        // when
        User result = userFacade.findOrCreateUser(optionalUser, nickname, email);

        // then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(nickname, result.getNickname());
    }

    @Test
    @DisplayName("")
    void updateUserNicknameTest() {
        // Arrange
        Long userId = 1L;
        String oldNickname = "강형준";
        String email = "email@gmail.com";
        String newNickname = "업데이트강형준";
        UpdateNicknameReqDto updateNicknameReqDto = new UpdateNicknameReqDto(newNickname);
        User updateMockUser = User.builder().id(userId).nickname(newNickname).email(email).build();
        when(userService.updateNickname(userId, newNickname)).thenReturn(updateMockUser);

        // when
        UserInfoResDto result = userFacade.updateUserNickname(userId, updateNicknameReqDto);

        // then
        assertNotNull(result);
        assertEquals(newNickname, result.nickname());
    }

    @Test
    @DisplayName("")
    void deleteUser_shouldReturnDeletedUserId() {
        // given
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn(userId);

        // when
        Long result = userFacade.deleteUser(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result);
    }
}