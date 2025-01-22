package com.groom.yummy.user;

import com.groom.yummy.exception.CustomException;
import com.groom.yummy.exception.UserErrorCode;
import com.groom.yummy.publisher.EventPublisher;
import com.groom.yummy.user.event.UserDeleteEvent;
import com.groom.yummy.user.event.UserNicknameChangedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Test
    @DisplayName("유저를 조회합니다.")
    void getUserInfoTest() {
        // given
        Long userId = 1L;
        String email = "email@gmail.com";
        String nickname = "깅형준";
        User mockUser = User.builder().id(userId).email(email).nickname(nickname).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        User result = userService.getUserInfo(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(email, result.getEmail());
        assertEquals(nickname, result.getNickname());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("유저 조회 결과가 Optional.empty 이면 예외가 발생합니다.")
    void getUserInfoFailTest() {
        // when
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when&then
        CustomException exception = assertThrows(CustomException.class, () -> userService.getUserInfo(userId));
        assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("유저의 닉네임을 변경합니다.")
    void updateNicknameTest() {
        // given
        Long userId = 1L;
        String email = "email@gmail.com";
        String oldNickname = "강형준";
        String newNickname = "newNickname";
        User mockUser = spy(User.builder().id(userId).email(email).nickname(oldNickname).build());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        User result = userService.updateNickname(userId, newNickname);

        // then
        assertNotNull(result);
        assertEquals(newNickname, result.getNickname());
        verify(mockUser, times(1)).changeNickname(newNickname);
        verify(eventPublisher, times(1)).publish(any(UserNicknameChangedEvent.class));
    }

    @Test
    @DisplayName("유저를 삭제(논리) 합니다.")
    void deleteUserTest() {
        // given
        Long userId = 1L;
        String email = "email@gmail.com";
        String nickname = "강형준";

        User mockUser = spy(User.builder().id(userId).email(email).nickname(nickname).build());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        Long result = userService.deleteUser(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result);
        verify(mockUser, times(1)).deleteUser();
        verify(eventPublisher, times(1)).publish(any(UserDeleteEvent.class));
    }

    @Test
    @DisplayName("없는 유저를 삭제하면 예외가 발생합니다.")
    void deleteUserFailTest() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when&then
        CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser(userId));
        assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).findById(userId);
    }

}
