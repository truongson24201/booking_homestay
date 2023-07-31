package website.booking_homestay.entity;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "districts")
@Data
public class Districts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Long districtId;
    @Column(name = "name")
    private String name;

//    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id",referencedColumnName = "province_id")
    private Provinces provinces;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "districts")
    private List<Wards> wards = new ArrayList<>();
//    @JsonIgnore
//    @OneToMany(mappedBy = "district")
//    private List<Branch> branches = new ArrayList<>();

}
