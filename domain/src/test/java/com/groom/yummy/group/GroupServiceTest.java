package com.groom.yummy.group;

import com.groom.yummy.exception.CustomException;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserRepository;
import com.groom.yummy.usertogroup.AttendanceStatus;
import com.groom.yummy.usertogroup.UserToGroupRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @DisplayName("사용자는 소모임을 생성할 수 있다.")
    @Order(1)
    @Test
    void 소모임_생성_테스트() {
        //Given
        User user = User.builder()
                .id(1L)
                .email("Goorm@gmail.com")
                .nickname("Goorm")
                .role("USER")
                .groupJoinCount(10L)
                .groupAttendanceCount(8L)
                .isDeleted(false)
                .build();

        Group group = Group.builder()
                .title("Go to Goorm store")
                .content("yummy yummy yummy yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .storeId(10L)
                .build();

        when(userRepository.findByEmail(Mockito.eq("Goorm@gmail.com")))
                .thenReturn(Optional.of(user));

        when(groupRepository.saveGroup(any(Group.class), anyLong()))
                .thenReturn(1L);

        // When
        Long groupId = groupService.createGroup(group, user.getEmail());

        // Then
        assertEquals(1L, groupId);
        verify(groupRepository, times(1))
                .saveGroup(any(Group.class), anyLong());
    }

    @DisplayName("사용자는 소모임 상세 정보를 조회할 수 있다.")
    @Order(2)
    @Test
    void 소모임_상세_조회_테스트() {
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

    @DisplayName("사용자는 소모임에 참여할 수 있다.")
    @Order(3)
    @Test
    void 소모임_참여_테스트() {
        // Given
        Long groupId = 1L;
        Long userId = 1L;
        String userEmail = "Goorm@gmail.com";

        Group group = Group.builder()
                .id(groupId)
                .title("Go to Goorm store")
                .content("yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .currentParticipants(2)
                .storeId(10L)
                .build();

        User user = User.builder()
                .id(userId)
                .email(userEmail)
                .nickname("Goorm")
                .role("USER")
                .groupJoinCount(10L)
                .groupAttendanceCount(8L)
                .isDeleted(false)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(groupRepository.findGroupById(groupId)).thenReturn(Optional.of(group));

        // When
        groupService.joinGroup(groupId, userId, userEmail);

        // Then
        verify(groupRepository, times(1)).updateGroupParticipants(eq(groupId), eq(3));
        verify(userToGroupRepository, times(1))
                .save(eq(group), eq(user), eq(false), eq(AttendanceStatus.APPROVED)); // save 호출 검증
    }


    @DisplayName("소모임 참여 시 참가 인원이 초과되면 예외가 발생한다.")
    @Order(4)
    @Test
    void 소모임_참여_참가인원_초과_테스트() {
        // Given
        Long groupId = 1L;
        Long userId = 1L;
        String userEmail = "Goorm@gmail.com";

        Group group = Group.builder()
                .id(groupId)
                .title("Go to Goorm store")
                .content("yummy yummy")
                .maxParticipants(4)
                .minParticipants(3)
                .currentParticipants(4)
                .storeId(10L)
                .build();

        User user = User.builder()
                .id(userId)
                .email(userEmail)
                .nickname("Goorm")
                .role("USER")
                .groupJoinCount(10L)
                .groupAttendanceCount(8L)
                .isDeleted(false)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(groupRepository.findGroupById(groupId)).thenReturn(Optional.of(group));

        // When
        CustomException exception = assertThrows(CustomException.class,
                () -> groupService.joinGroup(groupId, userId, userEmail));

        // Then
        assertEquals("참가 인원이 가득 찼습니다.", exception.getMessage());
        verify(groupRepository, never()).updateGroupParticipants(anyLong(), anyInt());
    }

    @DisplayName("소모임 리스트 조회 테스트")
    @Order(5)
    @Test
    void 소모임_리스트_조회_테스트() {
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
                        .minParticipants(3)
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
