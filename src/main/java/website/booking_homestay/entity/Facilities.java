package website.booking_homestay.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "facilities")
@Data
public class Facilities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long facilityId;
    @Column(name = "name")
    private String name;
//    @Column(name = "price")
//    private Double price;

    @JsonIgnore
    @ManyToMany(mappedBy = "facilities")
    private List<Homestay> homestays = new ArrayList<>();

    public Facilities(String name) {
        this.name = name;
//        this.price = price;
    }

    public Facilities() {
    }
}
