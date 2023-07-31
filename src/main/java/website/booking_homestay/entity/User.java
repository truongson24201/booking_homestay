package website.booking_homestay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long accountId;
//    @NotBlank
    @Column(name = "username")
    private String username;
//    @NotBlank
    @Column(name = "full_name")
    private String fullName;
//    @NotBlank
    @Column(name = "email")
    private String email;
//    @NotBlank
    @Column(name = "password")
    private String password;
//    @NotBlank
    @Column(name = "phone_number")
    private String phoneNumber;

//    @Column(name = "role_id")
//    private Long role_id;
    @Column(name = "activity")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",referencedColumnName = "role_id")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "refresh_token_id",referencedColumnName = "token_id")
    private RefreshToken refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id",referencedColumnName = "branch_id")
    private Branch branch;


    @OneToMany(mappedBy = "user")
    private List<Invoice> invoices = new ArrayList<>();


    public User(String username, String fullName, String email, String password, String phoneNumber, Role role) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = true;
    }

    public User() {
    }
}
