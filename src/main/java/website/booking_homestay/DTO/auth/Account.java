package website.booking_homestay.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Account {
    @NotBlank
    private String username;
    @NotBlank
    private String fullName;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
    private Long roleId;
}
