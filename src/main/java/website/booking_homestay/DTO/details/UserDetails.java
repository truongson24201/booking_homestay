package website.booking_homestay.DTO.details;

import lombok.Data;

@Data
public class UserDetails {
    private Long accountId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean status;
    private Long roleId;
    private Long branchId;
}
