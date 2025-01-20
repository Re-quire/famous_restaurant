package com.groom.yummy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.reply.Reply;
import com.groom.yummy.reply.ReplyService;
import com.groom.yummy.jwt.JwtProvider;
import com.groom.yummy.reply.dto.ReplyRequestDto;
import com.groom.yummy.reply.dto.ReplyUpdateRequestDto;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import jakarta.servlet.http.Cookie;

@WebMvcTest(ReplyController.class)
class ReplyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ReplyService replyService;

	@MockitoBean
	private JwtProvider jwtProvider;

	private Cookie authCookie;

	@BeforeEach
	void setUp() {
		// Mock 사용자 설정
		UserDetails principal = org.springframework.security.core.userdetails.User
			.withUsername("testUser@gmail.com")
			.password("password") // 비밀번호는 테스트용이므로 중요하지 않음
			.roles("USER") // 역할 설정
			.build();

		// 인증 객체 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			principal, principal.getPassword(), principal.getAuthorities()
		);

		// SecurityContext에 인증 정보 설정
		SecurityContextHolder.getContext().setAuthentication(authentication);

		when(jwtProvider.validateToken("valid.jwt.token")).thenReturn(true);
		when(jwtProvider.getUsername("valid.jwt.token")).thenReturn("testUser@gmail.com");

		// 쿠키 객체 생성
		authCookie = new Cookie("Authorization", "valid.jwt.token");
	}


	@Test
	@DisplayName("사용자는 댓글을 등록할 수 있다.")
	void registerReplyTest() throws Exception {
		// given
		Long groupId = 1L;
		String content = "댓글 내용";
		ReplyRequestDto requestDto = new ReplyRequestDto(content, null, 1L, groupId);
		Reply savedReply = Reply.builder().id(1L).content(content).groupId(groupId).build();

		// Mocking 서비스 호출
		when(replyService.createReply(any(Reply.class))).thenReturn(savedReply);

		String body = objectMapper.writeValueAsString(requestDto);

		// when
		ResultActions actions = mockMvc.perform(post("/api/v1/reply")
			.cookie(authCookie) // Authorization 쿠키 설정
			.with(csrf()) // CSRF 토큰 설정
			.contentType(MediaType.APPLICATION_JSON)
			.content(body));

		// then
		actions.andExpectAll(
			status().isOk(),
			jsonPath("$.data.id").value(savedReply.getId()),
			jsonPath("$.data.content").value(savedReply.getContent()),
			jsonPath("$.msg").value("댓글을 성공적으로 작성하였습니다.")
		);

		// verify 호출 횟수 검증
		verify(replyService, times(1)).createReply(any(Reply.class));
	}

	@Test
	@DisplayName("사용자는 특정 소모임의 댓글을 조회할 수 있다.")
	void getAllRepliesTest() throws Exception {
		// given
		Long groupId = 1L;
		Reply reply1 = Reply.builder().id(1L).content("댓글1").groupId(groupId).build();
		Reply reply2 = Reply.builder().id(2L).content("댓글2").groupId(groupId).build();
		Page<Reply> replyPage = new PageImpl<>(List.of(reply1, reply2));

		when(replyService.getAllReplies(eq(groupId), any())).thenReturn(replyPage);

		// when
		ResultActions actions = mockMvc.perform(get("/api/v1/reply/all")
			.param("groupId", groupId.toString())
			.param("page", "0")
			.param("size", "10")
			.cookie(authCookie)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpectAll(
			status().isOk(),
			jsonPath("$.data.content").isArray(),
			jsonPath("$.msg").value("댓글 조회 성공")
		);

		verify(replyService, times(1)).getAllReplies(eq(groupId), any());
	}

	@Test
	@DisplayName("사용자는 댓글을 수정할 수 있다.")
	void updateReplyTest() throws Exception {
		// given
		Long replyId = 1L;
		String updatedContent = "수정된 댓글 내용";
		ReplyUpdateRequestDto updateRequestDto = new ReplyUpdateRequestDto(updatedContent);
		Reply updatedReply = Reply.builder().id(replyId).content(updatedContent).build();

		when(replyService.updateReply(eq(replyId), eq(updatedContent))).thenReturn(updatedReply);

		String body = objectMapper.writeValueAsString(updateRequestDto);

		// when
		ResultActions actions = mockMvc.perform(patch("/api/v1/reply/" + replyId)
			.cookie(authCookie)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(body));

		// then
		actions.andExpectAll(
			status().isOk(),
			jsonPath("$.data.id").value(replyId),
			jsonPath("$.data.content").value(updatedContent),
			jsonPath("$.msg").value("댓글이 성공적으로 수정되었습니다.")
		);

		verify(replyService, times(1)).updateReply(eq(replyId), eq(updatedContent));
	}

	@Test
	@DisplayName("사용자는 댓글을 삭제할 수 있다.")
	void deleteReplyTest() throws Exception {
		// given
		Long replyId = 1L;

		doNothing().when(replyService).deleteReply(replyId);

		// when
		ResultActions actions = mockMvc.perform(delete("/api/v1/reply/" + replyId)
			.cookie(authCookie)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpectAll(
			status().isOk(),
			jsonPath("$.msg").value("댓글이 성공적으로 삭제되었습니다.")
		);

		verify(replyService, times(1)).deleteReply(replyId);
	}
}
