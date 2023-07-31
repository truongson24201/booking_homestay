package website.booking_homestay.DTO.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import website.booking_homestay.entity.enumreration.ECardType;
import website.booking_homestay.entity.enumreration.EInvoice;

import java.util.Date;

@Data
public class InvoiceDetails {
    private Long invoiceId;
    @JsonFormat(pattern = "dd/MM/yyyy'-'HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private Date create;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date checkIn;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date checkOut;
    private Double total;
    private String fullName;
    private String email;
    @Enumerated(EnumType.STRING)
    private String identityNumber;
    @Enumerated(EnumType.STRING)
    private ECardType cardType;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private EInvoice status;
    private Date updateOn;
    private String updateBy;
    private Long homestayId;
    private String name;
}
