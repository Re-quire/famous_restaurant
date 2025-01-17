package com.groom.yummy.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {
    private final UserRepository userRepository;

    public Optional<User> findAuthUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User findOrCreateUser(Optional<User> optionalUser, String nickname, String email){
        User user;
        if(optionalUser.isEmpty()){
            user = User.builder()
                    .nickname(nickname)
                    .email(email)
                    .role("ROLE_USER")
                    .groupJoinCount(0L)
                    .groupAttendanceCount(0L)
                    .isDeleted(false)
                    .build();
            userRepository.save(user);
        }else{
            user = optionalUser.get();
        }
        return user;
    }
}
