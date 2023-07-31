package website.booking_homestay.DTO.update;

import lombok.Data;

import java.util.Date;

@Data
public class HomestayUpdate {
    private String name;
    private Integer numPeople;
    private String status;
}
