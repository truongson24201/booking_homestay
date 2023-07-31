package website.booking_homestay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "branches")
@Data
//@Getter
//@Setter
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "map_location")
    private String mapLocation;

    @Column(name = "status")
    private Boolean status;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "address_id",referencedColumnName = "address_id")
//    private Address address;

//    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id",referencedColumnName = "province_id")
    private Provinces province;
//    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id",referencedColumnName = "district_id")
    private Districts district;
//    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id",referencedColumnName = "ward_id")
    private Wards ward;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
    @JoinTable(name = "branches_tourists",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "tourist_id"))
    private List<Tourist> tourists = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "branch")
    private List<Homestay> homestays = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "branch")
    private List<User> managers = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "branch")
    private List<PriceList> prices;
}
