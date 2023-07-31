package website.booking_homestay.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tourists")
@Data
public class Tourist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tourist_id")
    private Long touristId;

    @Column(name = "name")
    private String name;

//    @Column(name = "distance")
//    private Double distance;

    @JsonIgnore
    @ManyToMany(mappedBy = "tourists")
    private List<Branch> branches = new ArrayList<>();

    public Tourist(String name) {
        this.name = name;
    }

    public Tourist() {
    }
}
