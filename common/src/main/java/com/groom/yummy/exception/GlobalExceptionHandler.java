package com.groom.yummy.exception;

import com.groom.yummy.dto.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<Integer>> handleConstraintViolationException(ConstraintViolationException ex){
        log.error("유효성 검사 예외 발생 msg: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1, ex.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<Integer>> CustomException(CustomException ex){
        log.error("커스텀 예외 발생 msg: {}", ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getCode()).body(ResponseDto.of(-1,ex.getErrorCode().getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Integer>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError.getDefaultMessage();
        log.error("유효성 검사 예외 발생 msg:{}",message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1,message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<Integer>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "요청한 JSON 데이터를 읽을 수 없습니다: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1,errorMessage));
    }

    // 기타예외 발생 시 500반환
    @ExceptionHandler
    public ResponseEntity<ResponseDto> handleException(Exception ex) {

        String message = "서버 내부에 에러가 발생했습니다.";
        log.error(message+":"+ex.getMessage()+ex.getStackTrace()+ex.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(-1,ex.getMessage()));
    }
}

