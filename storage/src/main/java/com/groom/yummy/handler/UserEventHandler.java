package com.groom.yummy.handler;

import com.groom.yummy.domain.user.UserEntity;
import com.groom.yummy.domain.user.UserJpaRepository;
import com.groom.yummy.user.UserDeleteEvent;
import com.groom.yummy.user.UserNicknameChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserEventHandler {
    private final UserJpaRepository userJpaRepository;

    @EventListener
    public void handlerNicknameChange(UserNicknameChangedEvent event){
        UserEntity userEntity = userJpaRepository.findById(event.userId()).orElseThrow();
        userEntity.updateNickname(event.newNickname());
        log.info("userEntity nickname : {}", userEntity.getNickname());
    }

    @EventListener
    public void handleUserDelete(UserDeleteEvent event) {
        UserEntity userEntity = userJpaRepository.findById(event.userId()).orElseThrow();
        userEntity.deleteUser(event.isDeleted());
        log.info("userEntity isDelete : {}", userEntity.isDeleted());
    }
}
