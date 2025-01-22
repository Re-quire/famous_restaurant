package com.groom.yummy.reply;

import com.groom.yummy.exception.CustomException;
import com.groom.yummy.publisher.EventPublisher;
import com.groom.yummy.reply.event.ReplyUpdatedEvent;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserRepository;
import com.groom.yummy.group.Group;
import com.groom.yummy.group.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReplyServiceTest {

	@InjectMocks
	private ReplyService replyService;

	@Mock
	private ReplyRepository replyRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private GroupRepository groupRepository;

	@Mock
	private EventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("사용자는 소모임에 댓그을 등록할 수 있다.")
	void createReply_Success() {
		// Given
		Reply reply = Reply.builder().content("댓글 내용").userId(1L).groupId(1L).build();
		Reply savedReply = Reply.builder().id(1L).content("댓글 내용").userId(1L).groupId(1L).build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(mock(User.class)));
		when(groupRepository.findGroupById(1L)).thenReturn(Optional.of(mock(Group.class)));
		when(replyRepository.save(reply)).thenReturn(Optional.of(savedReply));

		// When
		Reply result = replyService.createReply(reply);

		// Then
		assertNotNull(result);
		assertEquals(savedReply.getId(), result.getId());
		verify(replyRepository, times(1)).save(reply);
	}

	@Test
	@DisplayName("존재하지 않는 사용자는 댓글을 등록할 수 없다.")
	void createReply_UserNotFound() {
		// Given
		Reply reply = Reply.builder().content("댓글 내용").userId(1L).groupId(1L).build();

		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		CustomException exception = assertThrows(CustomException.class, () -> {
			replyService.createReply(reply);
		});

		assertEquals("유저가 존재하지 않습니다.", exception.getMessage());
		verify(replyRepository, never()).save(any());
	}

	@Test
	@DisplayName("댓글을 수정할 수 있다.")
	void updateReply_Success() {
		// Given
		Reply reply = Reply.builder().id(1L).content("기존 댓글 내용").build();
		String updatedContent = "수정된 댓글 내용";

		when(replyRepository.findById(1L)).thenReturn(Optional.of(reply));

		// When
		Reply result = replyService.updateReply(1L, updatedContent);

		// Then
		assertNotNull(result);
		assertEquals(updatedContent, result.getContent());
		verify(replyRepository, times(1)).findById(1L);
		verify(eventPublisher, times(1)).publish(any(ReplyUpdatedEvent.class));
	}

	@Test
	@DisplayName("존재하지 않는 댓글은 수정할 수 없다.")
	void updateReply_ReplyNotFound() {
		// Given
		when(replyRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		CustomException exception = assertThrows(CustomException.class, () -> {
			replyService.updateReply(1L, "수정된 댓글 내용");
		});

		assertEquals("댓글이 존재하지 않습니다.", exception.getMessage());
		verify(eventPublisher, never()).publish(any());
	}

	@Test
	@DisplayName("소모임에 달리 모든 댓글을 조회할 수 있다.")
	void getAllReplies_Success() {
		// Given
		Long groupId = 1L;
		Pageable pageable = PageRequest.of(0, 10);
		Reply reply1 = Reply.builder().id(1L).content("댓글 1").groupId(groupId).build();
		Reply reply2 = Reply.builder().id(2L).content("댓글 2").groupId(groupId).build();
		Page<Reply> replyPage = new PageImpl<>(List.of(reply1, reply2));

		when(groupRepository.findGroupById(groupId)).thenReturn(Optional.of(mock(Group.class)));
		when(replyRepository.findByGroupId(eq(groupId), eq(pageable))).thenReturn(replyPage);

		// When
		Page<Reply> result = replyService.getAllReplies(groupId, pageable);

		// Then
		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		verify(replyRepository, times(1)).findByGroupId(eq(groupId), eq(pageable));
	}

	@Test
	@DisplayName("댓글을 삭제할 수 있다.")
	void deleteReply_Success() {
		// Given
		Long replyId = 1L;
		Reply reply = Reply.builder().id(replyId).content("댓글 내용").build();

		when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));

		// When
		replyService.deleteReply(replyId);

		// Then
		verify(replyRepository, times(1)).deleteById(replyId);
	}

	@Test
	@DisplayName("존재하지 않는 댓글은 삭제할 수 없다.")
	void deleteReply_ReplyNotFound() {
		// Given
		when(replyRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		CustomException exception = assertThrows(CustomException.class, () -> {
			replyService.deleteReply(1L);
		});

		assertEquals("댓글이 존재하지 않습니다.", exception.getMessage());
		verify(replyRepository, never()).deleteById(any());
	}
}