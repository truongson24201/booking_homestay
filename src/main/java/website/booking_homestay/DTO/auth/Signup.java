package website.booking_homestay.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Signup {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String fullName;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;
}
