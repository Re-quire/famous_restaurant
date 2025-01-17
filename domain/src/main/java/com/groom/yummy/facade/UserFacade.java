package com.groom.yummy.facade;

import com.groom.yummy.dto.request.UpdateNicknameReqDto;
import com.groom.yummy.dto.response.UserInfoResDto;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserAuthService;
import com.groom.yummy.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 서비스 계층과 충돌이 나지 않게 트랜잭션 선언
public class UserFacade {

    private final UserAuthService userAuthService;
    private final UserService userService;

    public UserInfoResDto getUserInfo(Long userId) {
        User user = userService.getUserInfo(userId);
        return UserInfoResDto.from(user);
    }
    public Optional<User> findAuthUserByEmail(String email){
        return userAuthService.findAuthUserByEmail(email);
    }

    @Transactional
    public User findOrCreateUser(Optional<User> optionalUser, String name, String email) {
        return userAuthService.findOrCreateUser(optionalUser, name, email);
    }

    @Transactional
    public UserInfoResDto updateUserNickname(Long id, UpdateNicknameReqDto updateNicknameReqDto){
        String nickname = updateNicknameReqDto.nickname();
        User user = userService.updateNickname(id, nickname);
        return UserInfoResDto.from(user);
    }

    @Transactional
    public Long deleteUser(Long userId) {
        Long deleteUserId = userService.deleteUser(userId);
        return deleteUserId;
    }
}
