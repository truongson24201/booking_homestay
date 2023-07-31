package website.booking_homestay.DTO.view;

import lombok.Data;

@Data
public class HomestayClient {
    private Long homestayId;
    private String name;
    private Integer numPeople;
    private Double price;
    private String images;
}
