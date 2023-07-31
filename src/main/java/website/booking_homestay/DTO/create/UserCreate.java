package website.booking_homestay.DTO.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreate {
    @NotBlank
    private String username;
    private String fullName;
    @NotBlank
    private String email;
    private String phoneNumber;
    private Long roleId;
}
