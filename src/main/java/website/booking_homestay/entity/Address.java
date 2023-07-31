//package website.booking_homestay.entity;
//
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name = "address")
//@Data
//public class Address {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "address_id")
//    private Long addressId;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "province_id",referencedColumnName = "province_id")
//    private Provinces province;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "district_id",referencedColumnName = "district_id")
//    private Districts district;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ward_id",referencedColumnName = "ward_id")
//    private Wards ward;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "branch_id",referencedColumnName = "branch_id")
//    private Branch branch;
//
//    public Address(Provinces province, Districts district, Wards ward, Branch branch) {
//        this.province = province;
//        this.district = district;
//        this.ward = ward;
//        this.branch = branch;
//    }
//}
