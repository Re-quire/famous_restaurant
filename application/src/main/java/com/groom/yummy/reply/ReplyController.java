package com.groom.yummy.reply;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groom.yummy.reply.dto.ReplyRequestDto;
import com.groom.yummy.reply.dto.ReplyResponseDto;
import com.groom.yummy.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reply")
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping
	public ResponseEntity<?> registerReply(@RequestBody ReplyRequestDto request) {
		try {

			Reply reply = request.toDomain();
			Reply savedReply = replyService.createReply(reply);
			ReplyResponseDto responseDto = ReplyResponseDto.fromDomain(savedReply);
			return ResponseEntity.ok(new ApiResponse<>(responseDto, "댓글을 성공적으로 작성하였습니다."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponse<>(-1, "댓글 등록 실패"));
		}
	}

}
