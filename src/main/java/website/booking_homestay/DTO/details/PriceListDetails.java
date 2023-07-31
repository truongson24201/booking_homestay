package website.booking_homestay.DTO.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PriceListDetails {
    private Long pricelistId;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date effectiveDate;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date updateOn;
    private String updateBy;
}
