package website.booking_homestay.DTO.view;

import lombok.Data;

@Data
public class UserView {
    private Long accountId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Boolean status;
}
