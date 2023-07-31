package website.booking_homestay.DTO.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class InvoiceView {
    private Long invoiceId;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date checkIn;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date checkOut;
    private Double total;
    //    @Enumerated(EnumType.STRING)
    private String fullName;
    private String phoneNumber;
    private Long homestayId;
    private String status;
}
