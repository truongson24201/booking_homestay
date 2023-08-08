package website.booking_homestay.DTO.details;

import lombok.Data;
import website.booking_homestay.entity.Facilities;
import website.booking_homestay.entity.Images;
import website.booking_homestay.entity.Tourist;

import java.util.List;

@Data
public class HomeClientDetail {
    private Long homestayId;
    private String name;
    private Integer numPeople;
    private Double price;
    private List<Images> images;
    private List<Facilities> facilities;
    private List<Tourist> tourists;
}
