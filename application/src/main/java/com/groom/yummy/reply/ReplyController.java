package com.groom.yummy.reply;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		Reply savedReply = replyService.createReply(request.toDomain());
		ReplyResponseDto responseDto = ReplyResponseDto.fromDomain(savedReply);
		return ResponseEntity.ok(new ApiResponse<>(responseDto, "댓글을 성공적으로 작성하였습니다."));
	}

	@GetMapping(path = "/all")
	public ResponseEntity<?> getAllReplies(@RequestParam Long groupId, Pageable pageable) {
		Page<Reply> replies = replyService.getAllReplies(groupId, pageable);
		Page<ReplyResponseDto> responseDtos = replies.map(ReplyResponseDto::fromDomain);
		return ResponseEntity.ok(new ApiResponse<>(responseDtos, "댓글 조회 성공."));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> updateReply(@PathVariable Long id, @Valid @RequestBody ReplyUpdateRequestDto request) {
		Reply updatedReply = replyService.updateReply(id, request.content());
		ReplyResponseDto responseDto = ReplyResponseDto.fromDomain(updatedReply);
		return ResponseEntity.ok(new ApiResponse<>(responseDto, "댓글이 성공적으로 수정되었습니다."));
	}
}
