package website.booking_homestay.DTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class TouristDTO {
    private Long touristId;
    private String name;
    private Double distance;
}
