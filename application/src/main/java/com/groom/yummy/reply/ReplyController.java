package com.groom.yummy.reply;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groom.yummy.reply.dto.ReplyRequestDto;
import com.groom.yummy.reply.dto.ReplyResponseDto;
import com.groom.yummy.reply.dto.ReplyUpdateRequestDto;
import com.groom.yummy.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reply")
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping
	public ResponseEntity<?> registerReply(@Valid @RequestBody ReplyRequestDto request) {
		try {
			Reply savedReply = replyService.createReply(request.toDomain());
			ReplyResponseDto responseDto = ReplyResponseDto.fromDomain(savedReply);
			return ResponseEntity.ok(new ApiResponse<>(responseDto, "댓글을 성공적으로 작성하였습니다."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponse<>(-1, "댓글 작성 실패: " + e.getMessage()));
		}
	}

	@GetMapping(path = "/all")
	public ResponseEntity<?> getAllReplies(
		@RequestParam Long groupId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		try {
			PageRequest pageRequest = PageRequest.of(page, size);
			Page<Reply> replies = replyService.getAllReplies(groupId, pageRequest);
			Page<ReplyResponseDto> responseDtos = replies.map(ReplyResponseDto::fromDomain);
			return ResponseEntity.ok(new ApiResponse<>(responseDtos, "댓글 조회 성공."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponse<>(-1, "댓글 조회 실패: " + e.getMessage()));
		}
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> updateReply(@PathVariable Long id, @Valid @RequestBody ReplyUpdateRequestDto request) {
		try {
			Reply updatedReply = replyService.updateReply(id, request.content());
			ReplyResponseDto responseDto = ReplyResponseDto.fromDomain(updatedReply);
			return ResponseEntity.ok(new ApiResponse<>(responseDto, "댓글이 성공적으로 수정되었습니다."));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponse<>(-1, "댓글 수정 실패: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse<>(-1, "서버 오류: " + e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteReply(@PathVariable Long id) {
		try {
			replyService.deleteReply(id);
			return ResponseEntity.ok(new ApiResponse<>(null, "댓글이 성공적으로 삭제되었습니다."));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponse<>(-1, "댓글 삭제 실패: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse<>(-1, "서버 오류: " + e.getMessage()));
		}
	}
}
