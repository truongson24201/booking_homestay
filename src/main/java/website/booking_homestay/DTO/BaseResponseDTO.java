package website.booking_homestay.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDTO {
    private HttpStatus code;
    private String message;
}
