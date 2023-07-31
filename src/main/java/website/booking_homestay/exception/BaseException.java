package website.booking_homestay.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseException extends RuntimeException{
    private HttpStatus code;
    private String message;
}
