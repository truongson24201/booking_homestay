package website.booking_homestay.DTO.view;

import lombok.Data;

@Data
public class PriceHomesDTO {
    private Long homestayId;
    private String name;
    private Integer numPeople;
    private String status;
}
