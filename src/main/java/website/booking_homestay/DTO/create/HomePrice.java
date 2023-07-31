package website.booking_homestay.DTO.create;

import lombok.Data;

import java.util.List;

@Data
public class HomePrice {
//    private Long homestayId;
    private List<Long> homestayId;
    private Double price;
}
