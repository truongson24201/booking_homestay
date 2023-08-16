package website.booking_homestay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "evaluation")
@IdClass(UserHomeId.class)
@Data
public class Evaluation {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "homestay_id")
    @JsonIgnore
    private Homestay homestay;

    @Column(name = "date_create")
    private Date create;

    @Column(name = "point")
    private Integer point;

    @Lob
    @Column(name = "content")
    private String content;

    public Evaluation(User user, Homestay homestay, Date create, Integer point, String content) {
        this.user = user;
        this.homestay = homestay;
        this.create = create;
        this.point = point;
        this.content = content;
    }

    public Evaluation() {
    }
}
