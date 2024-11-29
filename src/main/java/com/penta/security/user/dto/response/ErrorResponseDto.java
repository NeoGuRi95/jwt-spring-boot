package com.penta.security.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 에러 응답 DTO
 */
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private final int errorCode;

    private final String message;

    private final LocalDateTime time;

    private List<ValidationError> validErrors;

    public void addValidationError(String field, String message){
        if(Objects.isNull(validErrors)){
            validErrors = new ArrayList<>();
        }
        validErrors.add(new ValidationError(field, message));
    }
}

/**
 * 유효성 검사 오류
 */
@Data
@RequiredArgsConstructor
class ValidationError {

    private final String field;

    private final String message;
}