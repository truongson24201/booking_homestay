package website.booking_homestay.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "provinces")
@Data
public class Provinces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "province_id")
    private Long provinceId;

    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "provinces")
    private List<Districts> districts = new ArrayList<>();

//    @JsonIgnore
//    @OneToMany(mappedBy = "province")
//    private List<Branch> branches = new ArrayList<>();
//    @OneToOne(mappedBy = "province")
//    private Branch branch;

}
