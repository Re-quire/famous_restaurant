package com.groom.yummy.controller;

import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.group.GroupService;
import com.groom.yummy.group.dto.request.CreateGroupRequestDto;
import com.groom.yummy.group.dto.request.JoinGroupRequestDto;
import com.groom.yummy.group.dto.response.GroupDetailResponseDto;
import com.groom.yummy.group.dto.response.GroupResponseDto;
import com.groom.yummy.oauth2.auth.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "[Group] Group API")
@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "소모임 생성", description = "소모임을 생성합니다.")
    @PostMapping
    public ResponseEntity<ResponseDto<GroupResponseDto>> createGroup(
            @Valid @RequestBody CreateGroupRequestDto requestDto,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = loginUser.getUserId();
        Long groupId = groupService.createGroup(
                requestDto.getStoreId(),
                userId,
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getMaxParticipants(),
                requestDto.getMinParticipants(),
                requestDto.getMeetingDate()
        );

        GroupResponseDto response = groupService.findGroupById(groupId)
                .map(GroupResponseDto::fromGroupDomain)
                .orElseThrow();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(response, "소모임 생성 성공"));
    }

    @Operation(summary = "소모임 리스트 조회", description = "생성되어있는 소모임 리스트를 출력합니다.")
    @GetMapping
    public ResponseEntity<ResponseDto <List<GroupResponseDto>>> getAllGroups(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String regionCode,
            @RequestParam(required = false) String storeName,
            @RequestParam(defaultValue = "1") int page
    ) {
        List<GroupResponseDto> groups = groupService.getAllGroups(category, regionCode, storeName, page)
                .stream()
                .map(GroupResponseDto::fromGroupDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDto<>(groups, "소모임 목록 조회 성공"));
    }

    @Operation(summary = "소모임 상세 정보 조회", description = "소모임 ID 기반으로 소모임의 상세 정보를 출력합니다.")
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDto<GroupDetailResponseDto>> getGroupById(
            @PathVariable Long groupId
    ) {
        GroupDetailResponseDto response = groupService.findGroupById(groupId)
                .map(GroupDetailResponseDto::fromGroupDomain)
                .orElseThrow(() -> new IllegalArgumentException("소모임을 찾을 수 없습니다."));

        return ResponseEntity.ok(new ResponseDto<>(response, "소모임 상세 정보 조회 성공"));
    }

    @Operation(summary = "소모임 참가", description = "선택한 소모임에 참가합니다.")
    @PostMapping("/{groupId}/join")
    public ResponseEntity<ResponseDto<Void>> joinGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody JoinGroupRequestDto requestDto,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        Long userId = loginUser.getUserId();
        groupService.joinGroup(groupId, userId, requestDto.getStoreId());
        return ResponseEntity.ok(new ResponseDto<>(null, "소모임 참여 성공"));
    }
}

