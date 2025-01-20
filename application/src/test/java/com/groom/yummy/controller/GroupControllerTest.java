package com.groom.yummy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.group.Group;
import com.groom.yummy.group.GroupService;
import com.groom.yummy.group.MeetingStatus;
import com.groom.yummy.group.dto.request.CreateGroupRequestDto;
import com.groom.yummy.group.dto.request.JoinGroupRequestDto;
import com.groom.yummy.group.dto.response.GroupDetailResponseDto;
import com.groom.yummy.group.dto.response.GroupResponseDto;
import com.groom.yummy.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupController.class)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @DisplayName("사용자는 소모임을 생성할 수 있다.")
    @Test
    @Order(1)
    @WithMockUser(username = "testUser@gmail.com", roles = "USER")
    void 그룹_생성_테스트() throws Exception {
        // Given
        CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.builder()
                .title("Go to Goorm Store!")
                .content("yummy yummy yummy yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .meetingDate(LocalDateTime.now())
                .storeId(10L)
                .build();

        GroupResponseDto groupResponseDto = GroupResponseDto.builder()
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

        // Mock JwtProvider 동작 정의
        when(jwtProvider.validateToken("valid.jwt.token")).thenReturn(true);
        when(jwtProvider.getUsername("valid.jwt.token")).thenReturn("testUser@gmail.com");

        when(groupService.createGroup(any(Group.class), any(String.class))).thenReturn(1L);
        when(groupService.findGroupById(1L)).thenReturn(Optional.of(Group.builder()
                .id(1L)
                .title("Go to Goorm Store!")
                .content("yummy yummy yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .storeId(10L)
                .meetingStatus(MeetingStatus.OPEN)
                .build()));

        // When & Then
        mockMvc.perform(post("/api/v1/groups")
                        .cookie(new Cookie("Authorization", "valid.jwt.token"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createGroupRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("소모임 생성 성공"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Go to Goorm Store!"));
    }

    @DisplayName("사용자는 소모임 목록을 조회할 수 있다.")
    @Test
    @Order(2)
    @WithMockUser(username = "TestUser@gmail.com", roles = "USER")
    void 소모임_목록_리스트_조회_테스트() throws Exception {
        // Given
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

        when(groupService.getAllGroups(any(String.class), any(String.class), any(String.class), any(int.class)))
                .thenReturn(List.of(group));

        // When & Then
        mockMvc.perform(get("/api/v1/groups")
                        .param("category", "KOREAN")
                        .param("regionCode", "110110110")
                        .param("storeName", "구룸식당")
                        .param("page", "1")
                        .cookie(new Cookie("Authorization", "valid.jwt.token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("소모임 목록 조회 성공"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].title").value("Go to Goorm Store!"));
    }


    @DisplayName("사용자는 소모임 상세 정보를 조회할 수 있다.")
    @Test
    @Order(3)
    @WithMockUser(username = "testUser@gmail.com", roles = "USER")
    void 소모임_상세_조회_테스트() throws Exception {
        // Given
        GroupDetailResponseDto groupDetailResponseDto = GroupDetailResponseDto.builder()
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

        // When & Then
        mockMvc.perform(get("/api/v1/groups/{groupId}", 1L)
                        .cookie(new Cookie("Authorization", "valid.jwt.token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("소모임 상세 정보 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("Go to Goorm Store!"));
    }

    @DisplayName("사용자는 소모임에 참여할 수 있다.")
    @Test
    @Order(4)
    @WithMockUser(username = "testUser@gmail.com", roles = "USER")
    void 소모임_참가_테스트() throws Exception {
        // Given
        JoinGroupRequestDto joinGroupRequestDto = JoinGroupRequestDto.builder()
                .userId(1L)
                .build();

        // Mocking JwtProvider 동작 정의
        when(jwtProvider.validateToken("valid.jwt.token")).thenReturn(true);
        when(jwtProvider.getUsername("valid.jwt.token")).thenReturn("testUser@gmail.com");

        // When & Then
        mockMvc.perform(post("/api/v1/groups/{groupId}/join", 1L)
                        .cookie(new Cookie("Authorization", "valid.jwt.token"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinGroupRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("소모임 참여 성공"));
    }
}
