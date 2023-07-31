package website.booking_homestay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "images")
@Data
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;
    @Column(name = "url")
    private String url;

    public Images(String url) {
        this.url = url;
    }

    public Images() {

    }
}
