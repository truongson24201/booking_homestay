package website.booking_homestay.DTO.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class InvoiceView {
    private Long invoiceId;
    private Long homestayId;
    private String name;
    private Integer numPeople;
    private String address;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date checkIn;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date checkOut;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date updateOn;
    private Double total;
    //    @Enumerated(EnumType.STRING)
    private String fullName;
    private String email;
    private String phoneNumber;
    private String status;

    public InvoiceView(Long invoiceId, String name,Integer numPeople, String address, Date checkIn, Date checkOut, Date updateOn, Double total, String fullName,String email, String phoneNumber, String status) {
        this.invoiceId = invoiceId;
        this.name = name;
        this.numPeople = numPeople;
        this.address = address;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.updateOn = updateOn;
        this.total = total;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public InvoiceView() {
    }
}
