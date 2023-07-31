package website.booking_homestay.entity;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wards")
@Data
public class Wards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ward_id")
    private Long wardId;
    @Column(name = "name")
    private String name;

//    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id",referencedColumnName = "district_id")
    private Districts districts;
//    @JsonIgnore
//    @OneToMany(mappedBy = "ward")
//    private List<Branch> branches = new ArrayList<>();

}
