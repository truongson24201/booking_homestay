package website.booking_homestay.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long refreshTokenId;
    @Column(name = "token")
    private String token;
//    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "expiration")
    private Date expiration;


    public RefreshToken(String token, Date expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public RefreshToken() {
    }
}
