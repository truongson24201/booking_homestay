package website.booking_homestay.DTO.update;

import lombok.Data;

@Data
public class UserUpdate {
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean status;
    private Long roleId;
    private Long branchId;

}
