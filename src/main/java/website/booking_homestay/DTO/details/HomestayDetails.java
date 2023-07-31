package website.booking_homestay.DTO.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class HomestayDetails {
    private Long homestayId;
    private String name;
    private Integer numPeople;
    private String status;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date updateOn;
    private String updateBy;
}
