package com.groom.yummy.facade;

import com.groom.yummy.dto.request.UpdateNicknameReqDto;
import com.groom.yummy.dto.response.UserInfoResDto;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserAuthService;
import com.groom.yummy.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserAuthService userAuthService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserFacade userFacade;

    @Test
    void getUserInfoTest() {
        // given
        Long userId = 1L;
        String nickname = "강형준";
        String email = "email@gmail.com";

        User mockUser = User.builder().id(userId).nickname(nickname).email(email).build();
        Mockito.when(userService.getUserInfo(userId)).thenReturn(mockUser);

        // when
        UserInfoResDto result = userFacade.getUserInfo(userId);

        // then
        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.email());
        assertEquals(mockUser.getNickname(), result.nickname());
    }

    @Test
    void findAuthUserByEmailTest() {
        // given
        Long userId = 1L;
        String nickname = "강형준";
        String email = "email@gmail.com";

        User mockUser = User.builder().id(userId).nickname(nickname).email(email).build();
        Mockito.when(userAuthService.findAuthUserByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        Optional<User> result = userFacade.findAuthUserByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void findOrCreateUserTest() {
        // given
        Long userId = 1L;
        String nickname = "강형준";
        String email = "email@gmail.com";
        Optional<User> optionalUser = Optional.empty();
        User mockUser = User.builder().id(userId).nickname(nickname).email(email).build();
        Mockito.when(userAuthService.findOrCreateUser(optionalUser, nickname, email)).thenReturn(mockUser);

        // when
        User result = userFacade.findOrCreateUser(optionalUser, nickname, email);

        // then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(nickname, result.getNickname());
    }

    @Test
    void updateUserNicknameTest() {
        // Arrange
        Long userId = 1L;
        String oldNickname = "강형준";
        String email = "email@gmail.com";
        String newNickname = "업데이트강형준";
        UpdateNicknameReqDto updateNicknameReqDto = new UpdateNicknameReqDto(newNickname);
        User updateMockUser = User.builder().id(userId).nickname(newNickname).email(email).build();
        Mockito.when(userService.updateNickname(userId, newNickname)).thenReturn(updateMockUser);

        // when
        UserInfoResDto result = userFacade.updateUserNickname(userId, updateNicknameReqDto);

        // then
        assertNotNull(result);
        assertEquals(newNickname, result.nickname());
    }

    @Test
    void deleteUser_shouldReturnDeletedUserId() {
        // given
        Long userId = 1L;
        Mockito.when(userService.deleteUser(userId)).thenReturn(userId);

        // when
        Long result = userFacade.deleteUser(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result);
    }
}