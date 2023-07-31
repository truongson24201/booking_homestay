package website.booking_homestay.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import website.booking_homestay.entity.enumreration.EStatus;

import java.util.*;

@Entity
@Table(name = "homestays")
@Data
//@Getter
//@Setter
public class Homestay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestay_id")
    private Long homestayId;

    @Column(name = "name")
    private String name;

    @Column(name = "num_people")
    private Integer numPeople;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStatus status;

    @Column(name = "update_on")
    private Date updateOn;

    @Column(name = "update_by")
    private String updateBy;

//    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "homestays")
//    private List<Prices> prices;
    @Column(name = "flag")
    private Boolean flag;

    @JsonIgnore
    @OneToMany(mappedBy = "homestay",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<HomesPrices> homePrices = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "homestays_facilities",
            joinColumns = @JoinColumn(name = "homestay_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id"))
    private List<Facilities> facilities = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id",referencedColumnName = "branch_id")
    private Branch branch;


    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = false)
    @JoinColumn(name = "homestay_id")
    private List<Images> images = null;

    @OneToMany(mappedBy = "homestay")
    private List<Invoice> invoices = new ArrayList<>();

    public Homestay(String name, Integer numPeople) {
        this.name = name;
        this.numPeople = numPeople;
    }

    public Homestay() {
    }
}
