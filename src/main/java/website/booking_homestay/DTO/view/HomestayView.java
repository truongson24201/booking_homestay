package website.booking_homestay.DTO.view;

import lombok.Data;

import java.util.Date;

@Data
public class HomestayView {
    private Long homestayId;
    private String name;
    private Integer numPeople;
    private String status;
    private Date updateOn;
    private String updateBy;
}
