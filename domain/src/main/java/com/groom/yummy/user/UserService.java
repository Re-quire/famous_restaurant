package com.groom.yummy.user;

import com.groom.yummy.exception.CustomException;
import com.groom.yummy.exception.UserErrorCode;
import com.groom.yummy.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    public User getUserInfo(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public User updateNickname(Long userId, String nickname){
        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND));
        user.changeNickname(nickname);
        eventPublisher.publish(new UserNicknameChangedEvent(user.getId(),user.getNickname()));
        return user;
    }

    @Transactional
    public Long deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND));
        user.deleteUser();
        eventPublisher.publish(new UserDeleteEvent(user.getId(),user.isDeleted()));
        return user.getId();
    }
}
