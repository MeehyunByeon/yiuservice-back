package yiu.aisl.yiuservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity handleCustomException(CustomException ex) {
         ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity handleServerException(Exception ex) {
        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler({Exception.class})
//    protected ResponseEntity handleServerException(Exception ex) {
//        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    /*
//     * HTTP 400 Exception
//     */
//    @ExceptionHandler(value = NoSuchElementException.class)
//    protected ResponseEntity<ErrorResponse> handleNoSuchElementFoundException(final HttpClientErrorException.BadRequest e) {
//        log.error("handleBadRequest: {}", e.getMessage());
//        return ResponseEntity
//                .status(ErrorCode.INSUFFICIENT_DATA.getStatus().value())
//                .body(new ErrorResponse(ErrorCode.INSUFFICIENT_DATA));
//    }
//    /*
//     * HTTP 409 Exception
//     */
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    protected ResponseEntity<ErrorResponse> handleDuplicate(final HttpClientErrorException.BadRequest e) {
//        log.error("handleBadRequest: {}", e.getMessage());
//        return ResponseEntity
//                .status(ErrorCode.INSUFFICIENT_DATA.getStatus().value())
//                .body(new ErrorResponse(ErrorCode.INSUFFICIENT_DATA));
//    }
//
//    /*
//     * HTTP 409 Exception
//     */
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
//        log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
//        return ResponseEntity
//                .status(ErrorCode.CONFLICT.getStatus().value())
//                .body(new ErrorResponse(ErrorCode.CONFLICT));
//    }
//
//    /*
//     * HTTP 400 Exception
//     */
//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<ErrorResponse> handleBadRequest(final Exception e) {
//        log.error("handleBadRequest: {}", e.getMessage());
//        return ResponseEntity
//                .status(ErrorCode.INSUFFICIENT_DATA.getStatus().value())
//                .body(new ErrorResponse(ErrorCode.INSUFFICIENT_DATA));
//    }
//
//    /*
//     * HTTP 500 Exception
//     */
//    @ExceptionHandler(DataAccessException.class)
//    protected ResponseEntity<ErrorResponse> handleInternalServerError(final Exception e) {
//        log.error("handleInternalServerError: {}", e.getMessage());
//        return ResponseEntity
//                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
//                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
//    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> badRequest(final IllegalArgumentException ex){
//        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> conflict(final IllegalArgumentException ex){
//        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.CONFLICT);
//    }

}