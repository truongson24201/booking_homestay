package website.booking_homestay.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.Data;
import website.booking_homestay.entity.Districts;

@Data
public class WardDTO {
    private Long wardId;
    private String name;
    @JsonIgnore
    private DistrictDTO districts;
}
