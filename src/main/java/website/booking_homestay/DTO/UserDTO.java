package website.booking_homestay.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long accountId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Boolean status;
    private String role;
}
