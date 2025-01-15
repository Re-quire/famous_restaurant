package com.groom.yummy.domain.group;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupEntityRepository {
    private final GroupJpaRepository groupJpaRepository;
}
