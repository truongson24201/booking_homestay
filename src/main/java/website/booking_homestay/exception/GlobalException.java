package website.booking_homestay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import website.booking_homestay.DTO.BaseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponseDTO> handleBaseException(BaseException e) {
        BaseResponseDTO responseDTO = BaseResponseDTO.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(responseDTO.getCode()).body(responseDTO);
//        return ResponseEntity.ok(responseDTO);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BaseResponseDTO> handleMaxSizeException(MaxUploadSizeExceededException e) {
        BaseResponseDTO responseDTO = new BaseResponseDTO(HttpStatus.EXPECTATION_FAILED,"File too large!");
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseDTO);
    }
}
