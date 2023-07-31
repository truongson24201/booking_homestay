package website.booking_homestay.DTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ProvinceDTO {
    private Long provinceId;
    private String name;
}
