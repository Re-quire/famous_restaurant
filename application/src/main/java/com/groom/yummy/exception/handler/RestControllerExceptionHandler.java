package com.groom.yummy.exception.handler;

import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.exception.CustomException;
import io.swagger.v3.oas.annotations.Hidden;
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
@Hidden
@RestControllerAdvice
public class RestControllerExceptionHandler {

    /* 컨트롤러 메서드의 파라미터나 엔터티 필드에서 유효성 검사가 실패 */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<Integer>> handleConstraintViolationException(ConstraintViolationException ex){
        log.error("유효성 검사 예외 발생 msg: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1, ex.getMessage()));
    }

    /* @RequestBody 로 들어오는 JSON 요청 객체의 필드 값이 유효성 검사에 실패 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Integer>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError.getDefaultMessage();
        log.error("유효성 검사 예외 발생 msg:{}",message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1,message));
    }

    /* 클라이언트에서 잘못된 JSON 형식의 요청을 보낼 때 발생 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<Integer>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "요청한 JSON 데이터를 읽을 수 없습니다: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1,errorMessage));
    }
}

