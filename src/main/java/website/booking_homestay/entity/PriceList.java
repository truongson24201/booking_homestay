package website.booking_homestay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "priceList")
@Data
public class PriceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pricelist_id")
    private Long pricelistId;

    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.DATE)
    @Column(name = "effective_date")
    private Date effectiveDate;

    @JsonFormat(pattern = "dd/MM/yyyy'-'HH:mm", timezone = "Asia/Ho_Chi_Minh")
    @Column(name = "updateOn")
    private Date updateOn;

    @Column(name = "updateBy")
    private String updateBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id",referencedColumnName = "branch_id")
    private Branch branch;


//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
//    @JoinTable(name = "prices_homestays",
//            joinColumns = @JoinColumn(name = "price_id"),
//            inverseJoinColumns = @JoinColumn(name = "homestay_id"))
    @JsonIgnore
    @OneToMany(mappedBy = "priceList",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<HomesPrices> homePrices = new ArrayList<>();
//    private List<Homestay> homestays;


    public PriceList(Date effectiveDate, Date updateOn, String updateBy, Branch branch) {
        this.effectiveDate = effectiveDate;
        this.updateOn = updateOn;
        this.updateBy = updateBy;
        this.branch = branch;
    }

    public PriceList() {
    }
}
