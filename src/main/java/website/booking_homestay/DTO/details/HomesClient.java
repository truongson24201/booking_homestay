package website.booking_homestay.DTO.details;

import lombok.Data;
import website.booking_homestay.entity.Facilities;
import website.booking_homestay.entity.Images;
import website.booking_homestay.entity.Tourist;

import java.awt.*;
import java.util.List;

@Data
public class HomesClient {
    private Long homestayId;
    private String name;
    private Integer numPeople;
    private Double price;
    private String images;
//    private List<Facilities> facilities;
//    private List<Tourist> tourists;

    public HomesClient(Long homestayId, String name, Integer numPeople, Double price, Images images) {
        this.homestayId = homestayId;
        this.name = name;
        this.numPeople = numPeople;
        this.price = price;
        this.images = images.getUrl();
    }
}
