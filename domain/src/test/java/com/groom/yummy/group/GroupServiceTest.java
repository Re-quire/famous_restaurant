package com.groom.yummy.group;

import com.groom.yummy.exception.CustomException;
import com.groom.yummy.store.Store;
import com.groom.yummy.store.StoreRepository;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserRepository;
import com.groom.yummy.usertogroup.AttendanceStatus;
import com.groom.yummy.usertogroup.UserToGroupRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserToGroupRepository userToGroupRepository;

    @Mock
    private StoreRepository storeRepository;

    @DisplayName("User can create a group.")
    @Order(1)
    @Test
    void createGroupTest() {
        // Given
        Long storeId = 10L;
        Long userId = 1L;
        String title = "Go to Goorm store";
        String content = "yummy yummy yummy yummy yummy";
        Integer maxParticipants = 4;
        Integer minParticipants = 3;
        LocalDateTime meetingDate = LocalDateTime.now();

        User user = User.builder()
                .id(userId)
                .email("Goorm@gmail.com")
                .nickname("Goorm")
                .role("USER")
                .isDeleted(false)
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(groupRepository.saveGroup(any(Group.class)))
                .thenReturn(1L);

        // When
        Long groupId = groupService.createGroup(storeId, userId, title, content, maxParticipants, minParticipants, meetingDate);

        // Then
        assertEquals(1L, groupId);
        verify(groupRepository, times(1))
                .saveGroup(any(Group.class));
        verify(userToGroupRepository, times(1))
                .saveUserToGroup(any(Long.class), any(Long.class),  eq(true), eq(AttendanceStatus.APPROVED));
    }


    @DisplayName("User can view group details.")
    @Order(2)
    @Test
    void viewGroupDetailsTest() {
        // Given
        Long groupId = 1L;
        Group group = Group.builder()
                .id(groupId)
                .title("Go to Goorm store")
                .content("yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .currentParticipants(2)
                .storeId(10L)
                .build();

        when(groupRepository.findGroupById(groupId)).thenReturn(Optional.of(group));

        // When
        Optional<Group> foundGroup = groupService.findGroupById(groupId);

        // Then
        assertTrue(foundGroup.isPresent());
        assertEquals(groupId, foundGroup.get().getId());
        verify(groupRepository, times(1)).findGroupById(groupId);
    }

    @DisplayName("User can join a group.")
    @Order(3)
    @Test
    void joinGroupTest() {
        // Given
        Long groupId = 1L;
        Long userId = 1L;
        Long storeId = 10L;

        Group group = Group.builder()
                .id(groupId)
                .title("Go to Goorm store")
                .content("yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .currentParticipants(2)
                .storeId(storeId)
                .build();

        User user = User.builder()
                .id(userId)
                .email("Goorm@gmail.com")
                .nickname("Goorm")
                .role("USER")
                .isDeleted(false)
                .build();

        Store store = new Store(); // Store 객체 생성

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupRepository.findGroupById(groupId)).thenReturn(Optional.of(group));

        // Mock UserToGroupRepository 동작 설정
        doNothing().when(userToGroupRepository)
                .saveUserToGroup(eq(groupId), eq(userId), eq(false), eq(AttendanceStatus.APPROVED));

        // When
        groupService.joinGroup(groupId, userId, storeId);

        // Then
        verify(groupRepository, times(1)).updateGroupParticipants(eq(groupId), eq(3));
        verify(userToGroupRepository, times(1))
                .saveUserToGroup(eq(groupId), eq(userId), eq(false), eq(AttendanceStatus.APPROVED));
    }

    @DisplayName("Group join fails when participant limit exceeded.")
    @Order(4)
    @Test
    void joinGroupParticipantLimitExceededTest() {
        // Given
        Long groupId = 1L;
        Long userId = 1L;
        Long storeId = 10L;

        Group group = Group.builder()
                .id(groupId)
                .title("Go to Goorm store")
                .content("yummy yummy")
                .maxParticipants(4)
                .currentParticipants(4) // 참여 인원 초과
                .storeId(storeId)
                .build();

        User user = User.builder()
                .id(userId)
                .email("Goorm@gmail.com")
                .nickname("Goorm")
                .build();

        Store store = new Store(); // Store 객체 생성

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(groupRepository.findGroupById(groupId)).thenReturn(Optional.of(group));

        // When
        CustomException exception = assertThrows(CustomException.class,
                () -> groupService.joinGroup(groupId, userId, storeId));

        // Then
        assertEquals("참가 인원이 가득 찼습니다.", exception.getMessage());
        verify(groupRepository, never()).updateGroupParticipants(anyLong(), anyInt());
    }


    @DisplayName("Retrieve group list test.")
    @Order(5)
    @Test
    void retrieveGroupListTest() {
        // Given
        String category = "KOREAN";
        String regionCode = "SEOUL";
        String storeName = "Goorm Store";
        int page = 1;

        List<Group> groups = List.of(
                Group.builder()
                        .id(1L)
                        .title("Go to Goorm store")
                        .content("yummy yummy")
                        .maxParticipants(4)
                        .currentParticipants(2)
                        .storeId(10L)
                        .build()
        );

        when(groupRepository.findAllGroups(category, regionCode, storeName, page)).thenReturn(groups);

        // When
        List<Group> result = groupService.getAllGroups(category, regionCode, storeName, page);

        // Then
        assertEquals(1, result.size());
        assertEquals("Go to Goorm store", result.get(0).getTitle());
        verify(groupRepository, times(1)).findAllGroups(category, regionCode, storeName, page);
    }
}
