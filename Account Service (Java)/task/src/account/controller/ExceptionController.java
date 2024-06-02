package account.controller;

import account.service.CustomErrorMessage;
import account.service.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorMessage> handleCustomException(CustomException ex, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).substring(4));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
