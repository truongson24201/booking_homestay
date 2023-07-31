package website.booking_homestay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import website.booking_homestay.entity.enumreration.EInvoice;
import website.booking_homestay.entity.enumreration.ECardType;

import java.util.Date;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    @Column(name = "date_create")
    private Date create;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.DATE)
    @Column(name = "check_in")
    private Date checkIn;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.DATE)
    @Column(name = "check_out")
    private Date checkOut;
    @Column(name = "total")
    private Double total;
    @Column(name = "fullname")
    private String fullName;
    @Column(name = "email")
    private String email;
    @Column(name = "identity_number")
    private String identityNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "Card_type")
    private ECardType cardType;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EInvoice status;
    @Column(name = "update_on")
    private Date updateOn;
    @Column(name = "update_by")
    private String updateBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestay_id",referencedColumnName = "homestay_id")
    private Homestay homestay;

    public Invoice(Date create, Date checkIn, Date checkOut, Double total, String fullName, String email, String phoneNumber, EInvoice status, User user, Homestay homestay) {
        this.create = create;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.total = total;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.user = user;
        this.homestay = homestay;
    }

    public Invoice() {
    }
}
