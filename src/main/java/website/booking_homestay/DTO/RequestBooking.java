package website.booking_homestay.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class RequestBooking {
//    private Long homestayId;
    private Date checkIn;
    private Date checkOut;
//    private Integer numPeople;
//    private Double price;
}
