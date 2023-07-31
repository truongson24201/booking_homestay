package website.booking_homestay.DTO;

import lombok.Data;

@Data
public class AccountInforSendMail {
    private String username;

    private String password;

    private String email;

    private String name;

    public AccountInforSendMail(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public AccountInforSendMail() {
    }
}
