package com.groom.yummy.exception.handler;

import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.exception.CustomException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Hidden
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<Integer>> CustomException(CustomException ex){
        log.error("커스텀 예외 발생 msg: {}", ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getCode()).body(ResponseDto.of(-1,ex.getErrorCode().getMessage()));
    }

    // 기타예외 발생 시 500반환
    @ExceptionHandler
    public ResponseEntity<ResponseDto> handleException(Exception ex) {
        String message = "서버 내부에 에러가 발생했습니다.";
        log.error(message+":"+ex.getMessage()+ex.getStackTrace()+ex.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(-1,ex.getMessage()));
    }
}
