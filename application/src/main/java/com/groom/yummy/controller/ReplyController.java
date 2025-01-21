package com.groom.yummy.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.reply.Reply;
import com.groom.yummy.reply.ReplyService;
import com.groom.yummy.reply.dto.ReplyRequestDto;
import com.groom.yummy.reply.dto.ReplyResponseDto;
import com.groom.yummy.reply.dto.ReplyUpdateRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "[Reply] Reply API")
@RestController
@RequestMapping("/api/v1/reply")
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@Operation(
		summary = "댓글 등록",
		description = "댓글을 등록합니다.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "댓글 작성 성공",
				content = @Content(schema = @Schema(implementation = ReplyResponseDto.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "잘못된 요청 데이터"
			)
		}
	)
	@PostMapping
	public ResponseEntity<?> registerReply(@Valid @RequestBody ReplyRequestDto request) {
		try {
			Reply savedReply = replyService.createReply(request.toDomain());
			ReplyResponseDto responseDto = ReplyResponseDto.fromDomain(savedReply);
			return ResponseEntity.ok(new ResponseDto<>(responseDto, "댓글을 성공적으로 작성하였습니다."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseDto<>(-1, "댓글 작성 실패: " + e.getMessage()));
		}
	}

	@Operation(
		summary = "댓글 목록 조회",
		description = "특정 그룹 ID에 속한 모든 댓글을 페이지 단위로 조회합니다.",
		parameters = {
			@Parameter(name = "groupId", description = "그룹 ID", required = true),
			@Parameter(name = "page", description = "조회할 페이지 번호 (0부터 시작)", example = "0"),
			@Parameter(name = "size", description = "페이지 크기", example = "10")
		},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "댓글 조회 성공",
				content = @Content(schema = @Schema(implementation = ReplyResponseDto.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "잘못된 요청 데이터"
			)
		}
	)
	@GetMapping(path = "/all")
	public ResponseEntity<?> getAllReplies(
		@RequestParam Long groupId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		try {
			PageRequest pageRequest = PageRequest.of(page, size);
			Page<Reply> replies = replyService.getAllReplies(groupId, pageRequest);
			Page<ReplyResponseDto> responseDtos = replies.map(ReplyResponseDto::fromDomain);
			return ResponseEntity.ok(new ResponseDto<>(responseDtos, "댓글 조회 성공"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseDto<>(-1, "댓글 조회 실패: " + e.getMessage()));
		}
	}

	@Operation(
		summary = "댓글 수정",
		description = "댓글의 내용을 수정합니다.",
		parameters = {
			@Parameter(name = "id", description = "수정할 댓글 ID", required = true)
		},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "댓글 수정 성공",
				content = @Content(schema = @Schema(implementation = ReplyResponseDto.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "잘못된 요청 데이터 또는 댓글이 존재하지 않음"
			),
			@ApiResponse(
				responseCode = "500",
				description = "서버 오류"
			)
		}
	)
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateReply(@PathVariable Long id, @Valid @RequestBody ReplyUpdateRequestDto request) {
		try {
			Reply updatedReply = replyService.updateReply(id, request.content());
			ReplyResponseDto responseDto = ReplyResponseDto.fromDomain(updatedReply);
			return ResponseEntity.ok(new ResponseDto<>(responseDto, "댓글이 성공적으로 수정되었습니다."));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseDto<>(-1, "댓글 수정 실패: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseDto<>(-1, "서버 오류: " + e.getMessage()));
		}
	}

	@Operation(
		summary = "댓글 삭제",
		description = "댓글을 삭제합니다.",
		parameters = {
			@Parameter(name = "id", description = "삭제할 댓글 ID", required = true)
		},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "댓글 삭제 성공"
			),
			@ApiResponse(
				responseCode = "400",
				description = "잘못된 요청 데이터 또는 댓글 삭제 실패"
			),
			@ApiResponse(
				responseCode = "500",
				description = "서버 오류"
			)
		}
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteReply(@PathVariable Long id) {
		try {
			replyService.deleteReply(id);
			return ResponseEntity.ok(new ResponseDto<>(null, "댓글이 성공적으로 삭제되었습니다."));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseDto<>(-1, "댓글 삭제 실패: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseDto<>(-1, "서버 오류: " + e.getMessage()));
		}
	}
}
