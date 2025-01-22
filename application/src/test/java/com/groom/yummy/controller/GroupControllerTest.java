package com.groom.yummy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.group.Group;
import com.groom.yummy.group.GroupService;
import com.groom.yummy.group.MeetingStatus;
import com.groom.yummy.group.dto.request.CreateGroupRequestDto;
import com.groom.yummy.group.dto.request.JoinGroupRequestDto;
import com.groom.yummy.group.dto.response.GroupDetailResponseDto;
import com.groom.yummy.jwt.JwtProvider;
import com.groom.yummy.oauth2.auth.LoginUser;
import com.groom.yummy.user.User;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private Cookie authCookie;

    @BeforeEach
    void setUp() {
        // Mock User 객체 생성
        User mockUser = User.builder()
                .id(1L)
                .email("testUser@gmail.com")
                .nickname("testNickname")
                .role("USER")
                .build();

        // Mock LoginUser 생성
        LoginUser mockLoginUser = new LoginUser(mockUser);

        // 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockLoginUser, null, List.of())
        );

        // JWT 검증
        when(jwtProvider.validateToken("valid.jwt.token")).thenReturn(true);
        when(jwtProvider.getUsername("valid.jwt.token")).thenReturn("testUser@gmail.com");

        // Authorization 쿠키 생성
        authCookie = new Cookie("Authorization", "valid.jwt.token");
    }

    @Test
    @Order(1)
    @DisplayName("사용자는 소모임을 생성할 수 있다.")
    void 소모임_생성_테스트() throws Exception {
        // given
        CreateGroupRequestDto requestDto = CreateGroupRequestDto.builder()
                .title("Go to Goorm Store!")
                .content("yummy yummy yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .meetingDate(LocalDateTime.now())
                .storeId(10L)
                .build();

        // Mock groupId 반환값
        Long mockGroupId = 1L;

        // Mock 그룹 생성 동작
        when(groupService.createGroup(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mockGroupId);

        // Mock findGroupById 반환값 설정
        Group mockGroup = Group.builder()
                .id(mockGroupId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .maxParticipants(requestDto.getMaxParticipants())
                .minParticipants(requestDto.getMinParticipants())
                .currentParticipants(1)
                .meetingDate(requestDto.getMeetingDate())
                .meetingStatus(MeetingStatus.OPEN)
                .storeId(requestDto.getStoreId())
                .build();

        when(groupService.findGroupById(mockGroupId)).thenReturn(Optional.of(mockGroup));

        String body = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/groups")
                .cookie(authCookie)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        actions.andExpectAll(
                status().isCreated(),
                jsonPath("$.msg").value("소모임 생성 성공"),
                jsonPath("$.data.id").value(1L)
        );

        // 호출 횟수 검증
        verify(groupService, times(1)).createGroup(any(), any(), any(), any(), any(), any(), any());
        verify(groupService, times(1)).findGroupById(mockGroupId);
    }


    @Test
    @Order(2)
    @DisplayName("사용자는 소모임 목록을 조회할 수 있다.")
    void 소모임_리스트_조회_테스트() throws Exception {
        // given
        Group group = Group.builder()
                .id(1L)
                .title("Go to Goorm Store!")
                .content("yummy yummy yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .currentParticipants(1)
                .meetingDate(LocalDateTime.now())
                .meetingStatus(MeetingStatus.OPEN)
                .storeId(10L)
                .build();

        when(groupService.getAllGroups(any(), any(), any(), anyInt()))
                .thenReturn(List.of(group));

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/groups")
                .param("category", "KOREAN")
                .param("regionCode", "110110110")
                .param("storeName", "구룸식당")
                .param("page", "1")
                .cookie(authCookie));

        // then
        actions.andExpectAll(
                status().isOk(),
                jsonPath("$.msg").value("소모임 목록 조회 성공"),
                jsonPath("$.data[0].id").value(1L),
                jsonPath("$.data[0].title").value("Go to Goorm Store!")
        );

        verify(groupService, times(1)).getAllGroups(any(), any(), any(), anyInt());
    }

    @Test
    @Order(3)
    @DisplayName("사용자는 소모임 상세 정보를 조회할 수 있다.")
    void 소모임_상세정보_조회_테스트() throws Exception {
        // given
        GroupDetailResponseDto responseDto = GroupDetailResponseDto.builder()
                .id(1L)
                .title("Go to Goorm Store!")
                .content("yummy yummy yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .currentParticipants(1)
                .meetingDate(LocalDateTime.now())
                .meetingStatus("OPEN")
                .storeId(10L)
                .createdAt(LocalDateTime.now())
                .build();

        when(groupService.findGroupById(1L)).thenReturn(Optional.of(Group.builder()
                .id(1L)
                .title("Go to Goorm Store!")
                .content("yummy yummy yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .currentParticipants(1)
                .meetingDate(LocalDateTime.now())
                .meetingStatus(MeetingStatus.OPEN)
                .storeId(10L)
                .build()));

        // when
        ResultActions actions = mockMvc.perform(get("/api/v1/groups/{groupId}", 1L)
                .cookie(authCookie));

        // then
        actions.andExpectAll(
                status().isOk(),
                jsonPath("$.msg").value("소모임 상세 정보 조회 성공"),
                jsonPath("$.data.id").value(1L),
                jsonPath("$.data.title").value("Go to Goorm Store!")
        );

        verify(groupService, times(1)).findGroupById(1L);
    }

    @Test
    @Order(4)
    @DisplayName("사용자는 소모임에 참여할 수 있다.")
    void 소모임_가입_테스트() throws Exception {
        // given
        Long groupId = 1L;
        Long storeId = 10L;

        JoinGroupRequestDto joinRequestDto = JoinGroupRequestDto.builder()
                .userId(1L)
                .storeId(storeId)
                .build();

        doNothing().when(groupService).joinGroup(eq(groupId), any(), eq(storeId));

        String body = objectMapper.writeValueAsString(joinRequestDto);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/groups/{groupId}/join", groupId)
                .cookie(authCookie)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        actions.andExpectAll(
                status().isOk(),
                jsonPath("$.msg").value("소모임 참여 성공")
        );

        verify(groupService, times(1)).joinGroup(eq(groupId), any(), eq(storeId));
    }
}
