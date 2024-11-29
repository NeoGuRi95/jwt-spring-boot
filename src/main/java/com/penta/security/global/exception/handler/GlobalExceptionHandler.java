package com.penta.security.global.exception.handler;

import com.penta.security.global.exception.PasswordIncorrectException;
import com.penta.security.global.exception.RefreshTokenInvalidException;
import com.penta.security.global.exception.UserIdNotFoundException;
import com.penta.security.user.dto.response.ErrorResponseDto;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 전역 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
        HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(100, ex.getMessage(),
            LocalDateTime.now());

        return ResponseEntity.status(statusCode).body(errorResponseDto);
    }

    /**
     * 모든 예외 처리
     *
     * @param ex      발생한 예외
     * @param request 요청
     * @return ErrorResponse 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("Internal Server Error occurred", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(100, ex.getMessage(),
            LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

    /**
     * 유효성 검사 예외 처리
     *
     * @param ex      MethodArgumentNotValidException
     * @param headers 헤더
     * @param status  상태
     * @param request 요청
     * @return ErrorResponse 응답
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
        WebRequest request) {
        log.error("Method Argument Not Valid Exception occurred", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
            200,
            "요청 데이터가 유효하지 않습니다. validErrors 필드를 확인하세요.", LocalDateTime.now());

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponseDto.addValidationError(fieldError.getField(),
                fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponseDto);
    }

    /**
     * 인증 실패 예외 처리
     *
     * @param ex      AuthorizationDeniedException
     * @param request 요청
     * @return 인ErrorResponse 응답
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorizationDeniedException(AuthorizationDeniedException ex,
        WebRequest request) {
        log.error("Authorization Denied Exception occurred", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(300, ex.getMessage(),
            LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }

    /**
     * Refresh Token 유효성 검증 예외 처리
     *
     * @param ex      RefreshTokenInvalidException
     * @param request 요청
     * @return ErrorResponse 응답
     */
    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<ErrorResponseDto> handleRefreshTokenInvalidException(RefreshTokenInvalidException ex,
        WebRequest request) {
        log.error("Refresh Token Invalid Exception occurred", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(301, ex.getMessage(),
            LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }

    /**
     * Access Token 회원 ID 찾지 못한 경우 예외 처리
     *
     * @param ex      UsernameNotFoundException
     * @param request 요청
     * @return ErrorResponse 응답
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFoundException(UsernameNotFoundException ex,
        WebRequest request) {
        log.error("Username Not Found Exception occurred", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(302, ex.getMessage(),
            LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }

    /**
     * 로그인에서 회원 ID 찾지 못한 경우 예외 처리
     *
     * @param ex      UserIdNotFoundException
     * @param request 요청
     * @return ErrorResponse 응답
     */
    @ExceptionHandler(UserIdNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserIdNotFoundException(UserIdNotFoundException ex,
        WebRequest request) {
        log.error("User Id Not Found Exception occurred", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(303, ex.getMessage(),
            LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }

    /**
     * 비밀번호 불일치 예외 처리
     *
     * @param ex      PasswordIncorrectException
     * @param request 요청
     * @return ErrorResponse 응답
     */
    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ErrorResponseDto> handlePasswordIncorrectException(PasswordIncorrectException ex,
        WebRequest request) {
        log.error("Password Incorrect Exception occurred", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(304, ex.getMessage(),
            LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }
}