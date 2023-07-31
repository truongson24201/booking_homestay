package website.booking_homestay.entity;

import jakarta.persistence.*;
import lombok.Data;
import website.booking_homestay.entity.enumreration.ERole;

@Data
@Entity
@Table(name = "roles",uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
//    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ERole name;

    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }

    public Role() {
    }
}
