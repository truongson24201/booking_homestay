package website.booking_homestay.DTO.create;

import lombok.Data;

import java.util.Date;

@Data
public class InvoiceCreate {
//    private Long homestayId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Date checkIn;
    private Date checkOut;
//    private Double total;


}
