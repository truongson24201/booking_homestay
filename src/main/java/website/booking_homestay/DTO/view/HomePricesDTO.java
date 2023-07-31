package website.booking_homestay.DTO.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class HomePricesDTO {
    private Long priceId;
    private Double price;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date effectiveDate;
    private String status;

}
