package com.groom.yummy.handler;

import com.groom.yummy.domain.user.UserEntity;
import com.groom.yummy.domain.user.UserJpaRepository;
import com.groom.yummy.user.event.UserDeleteEvent;
import com.groom.yummy.user.event.UserNicknameChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserEventHandler {
    private final UserJpaRepository userJpaRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlerNicknameChange(UserNicknameChangedEvent event){
        UserEntity userEntity = userJpaRepository.findById(event.userId()).orElseThrow();
        userEntity.updateNickname(event.newNickname());
        log.info("userEntity nickname : {}", userEntity.getNickname());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleUserDelete(UserDeleteEvent event) {
        UserEntity userEntity = userJpaRepository.findById(event.userId()).orElseThrow();
        userEntity.deleteUser(event.isDeleted());
        log.info("userEntity isDelete : {}", userEntity.isDeleted());
    }
}
